package com.elastic.demo.verticle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class SearchVerticle extends AbstractVerticle {

	private RestHighLevelClient elasticSearchClient;

	@Override
	public void start(Future<Void> startFuture) throws Exception {

		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());

		router.get("/ping").handler(this::ping);

		router.post("/index").handler(this::indexDocument);
		router.put("/index").handler(this::updateDocument);
		router.delete("/index").handler(this::deleteDocument);
		router.get("/index").handler(this::checkIfExists);
		router.get("/index/search").handler(this::getWithPagination);
		router.get("/index/:field/:value").handler(this::getDocumentByField);

		vertx.createHttpServer().requestHandler(router).listen(8080, handler -> {

			if (handler.succeeded()) {
				startFuture.complete();
			} else {
				startFuture.fail("Error while creating http server");
			}
		});

		elasticSearchClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

	}

	@Override
	public void stop() throws Exception {

		if (null != elasticSearchClient) {
			elasticSearchClient.close();
		}
	}

	private void ping(RoutingContext context) {
		context.request().response().setStatusCode(200).end("hello");

	}

	private void indexDocument(RoutingContext context) {

		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			JsonObject jsonRequest = context.getBodyAsJson();
			jsonMap.put("merchant_id", jsonRequest.getString("merchant_id"));
			jsonMap.put("merchant_name", jsonRequest.getString("merchant_name"));
			jsonMap.put("transaction_type", jsonRequest.getString("transaction_type"));
			jsonMap.put("card_number", jsonRequest.getString("card_number"));
			jsonMap.put("cvv", jsonRequest.getString("cvv"));
			jsonMap.put("exp_dt", jsonRequest.getString("exp_dt"));
			jsonMap.put("transaction_origin", jsonRequest.getString("transaction_origin"));
			jsonMap.put("customer_name", jsonRequest.getString("customer_name"));
			jsonMap.put("customer_email_id", jsonRequest.getString("customer_email_id"));
			jsonMap.put("shipping_address", jsonRequest.getString("shipping_address"));
			jsonMap.put("cuurency_code", jsonRequest.getString("cuurency_code"));
			jsonMap.put("amount", jsonRequest.getString("amount"));
			jsonMap.put("transaction_date", new Date());
			IndexRequest indexRequest = new IndexRequest("payment_transactions")
					.id(context.getBodyAsJson().getString("transaction_id")).source(jsonMap);
			indexRequest.opType(DocWriteRequest.OpType.CREATE);
			IndexResponse response;
			response = elasticSearchClient.index(indexRequest, RequestOptions.DEFAULT);

			context.request().response().setStatusCode(200).end(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
			context.request().response().setStatusCode(500).end(new JsonObject(e.getMessage()).encodePrettily());

		}

	}

	private void updateDocument(RoutingContext context) {

		try {
			String id = context.request().getParam("id");
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			JsonObject jsonRequest = context.getBodyAsJson();
			jsonMap.put("shipping_address", jsonRequest.getString("shipping_address"));
			jsonMap.put("transaction_date", new Date());
			UpdateRequest request = new UpdateRequest("payment_transactions", id).doc(jsonMap, XContentType.JSON);
			UpdateResponse response = elasticSearchClient.update(request, RequestOptions.DEFAULT);
			context.request().response().setStatusCode(200).putHeader("content-type", "applilcation/json")
					.end(response.toString());

		} catch (Exception e) {
			context.request().response().setStatusCode(500).end(new JsonObject(e.getMessage()).encodePrettily());
		}

	}

	private void deleteDocument(RoutingContext context) {
		try {
			String id = context.request().getParam("id");
			DeleteRequest deleteRequest = new DeleteRequest("payment_transactions", id);
			DeleteResponse response = elasticSearchClient.delete(deleteRequest, RequestOptions.DEFAULT);
			context.request().response().setStatusCode(200).putHeader("content-type", "applilcation/json")
					.end(response.toString());
		} catch (Exception e) {
			context.request().response().setStatusCode(500).end(new JsonObject(e.getMessage()).encodePrettily());
		}

	}

	private void checkIfExists(RoutingContext context) {
		try {
			String id = context.request().getParam("id");
			Long version = new Long(context.request().getParam("version"));
			GetRequest getRequest = new GetRequest("payment_transactions", id).version(version);
			getRequest.fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE);
			GetResponse response = elasticSearchClient.get(getRequest, RequestOptions.DEFAULT);
			context.request().response().setStatusCode(200).putHeader("content-type", "applilcation/json")
					.end(response.toString());
		} catch (Exception e) {
			e.printStackTrace();
			context.request().response().setStatusCode(500).end(e.getMessage());

		}

	}

	private void getWithPagination(RoutingContext context) {

		try {
			SearchRequest request = new SearchRequest("payment_transactions");
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.matchAllQuery());
			searchSourceBuilder.from(0);
			searchSourceBuilder.size(5);
			request.source(searchSourceBuilder);
			SearchResponse response = elasticSearchClient.search(request, RequestOptions.DEFAULT);
			context.request().response().setStatusCode(200).putHeader("content-type", "applilcation/json")
					.end(response.toString());
		} catch (Exception e) {
			context.request().response().setStatusCode(500).end(e.getMessage());
		}

	}

	private void getDocumentByField(RoutingContext context) {

		String field = context.request().getParam("field");
		String value = context.request().getParam("value");

		try {
			SearchRequest request = new SearchRequest("payment_transactions");
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder(field, value);
			searchSourceBuilder.query(matchQueryBuilder);
			request.source(searchSourceBuilder);
			SearchResponse response = elasticSearchClient.search(request, RequestOptions.DEFAULT);
			context.request().response().setStatusCode(200).putHeader("content-type", "applilcation/json")
					.end(response.toString());
		} catch (Exception e) {
			context.request().response().setStatusCode(500).end(e.getMessage());
		}

	}

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();

		vertx.deployVerticle(SearchVerticle.class.getName(), handler -> {
			if (handler.succeeded()) {
				System.out.println("Verticle deployed successfully");
			} else {
				System.out.println(handler.result());
			}

		});
	}
}
