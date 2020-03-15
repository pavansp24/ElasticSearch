# ElasticSearch

Elastic Search Using Vertx. 


Step:1
docker pull docker.elastic.co/elasticsearch/elasticsearch:7.6.1

Step:2
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.6.1

Step:3
docker pull docker.elastic.co/kibana/kibana:7.6.1

Step:4
docker run --link nervous_ardinghelli:elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:7.6.1

Step:5
Browse below URL to validate the elastic search container is up and running
http://localhost:9200/

Confirm below response in the browser
{
  "name" : "93b7de99150f",
  "cluster_name" : "docker-cluster",
  "cluster_uuid" : "v4R3LsYNQOSJKOpXMMWTLQ",
  "version" : {
    "number" : "7.6.1",
    "build_flavor" : "default",
    "build_type" : "docker",
    "build_hash" : "aa751e09be0a5072e8570670309b1f12348f023b",
    "build_date" : "2020-02-29T00:15:25.529771Z",
    "build_snapshot" : false,
    "lucene_version" : "8.4.0",
    "minimum_wire_compatibility_version" : "6.8.0",
    "minimum_index_compatibility_version" : "6.0.0-beta1"
  },
  "tagline" : "You Know, for Search"
}

Step:6

Browse below URL to validate the kibana container is up and running

http://localhost:5601

Kibana Dashboard will be displayed.



Curl Commands:

1 Create Document

curl -X POST \
  http://localhost:8080/index \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 430' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: b08febef-9dd1-4147-b023-62631639a897,64314cdf-b120-4734-ad35-0c341d758328' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache' \
  -d '{
    "merchant_id": "007",
    "merchant_name": "desifood",
    "transaction_type": "CNP",
    "card_number": "4214010103025487",
    "cvv": "007",
    "exp_dt": "0721",
    "transaction_origin": "mobile_androide",
    "customer_name": "nameless wonder",
    "customer_email_id": "nameless@wonder.com",
    "shipping_address": "#32,Mars",
    "cuurency_code": "IND",
    "amount": "50",
    "transaction_id": "0071584274971764"
}'

IndexResponse[index=payment_transactions,type=_doc,id=0071584274971764,version=1,result=created,seqNo=7,primaryTerm=1,shards={"total":2,"successful":1,"failed":0}]


2 Check if document exists

curl -X GET \
  'http://localhost:8080/index?id=0071584273161887&version=2' \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: d49fbd67-cd4a-4d55-8519-60dca4f55b23,15c7facd-65a7-4878-a462-5b456f45788a' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache'
  
  {
    "_index": "payment_transactions",
    "_type": "_doc",
    "_id": "0071584273161887",
    "_version": 2,
    "_seq_no": 4,
    "_primary_term": 1,
    "found": true
}


 3. Delete a document
 
curl -X DELETE \
  'http://localhost:8080/index?id=0071584271856332' \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: ' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 2ddc3896-9cb7-4155-89dd-5826d022f20b,b0b2b611-c6bc-4947-87a3-acea964c702c' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache'
  
  DeleteResponse[index=payment_transactions,type=_doc,id=0071584271856332,version=2,result=deleted,shards=ShardInfo{total=2, successful=1, failures=[]
    }
]
  
4 Update a document

curl -X PUT \
  'http://localhost:8080/index?id=0071584273161887' \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Length: 71' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 6d02f5fd-9e72-42be-812d-47a4fccb9344,135d4a77-6a08-41a3-986e-65d1325926a6' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache' \
  -d '{
	    "shipping_address": "#32, Elong must colony. Tesla city. Mars"
}'

UpdateResponse[index=payment_transactions,type=_doc,id=0071584273161887,version=2,seqNo=4,primaryTerm=1,result=updated,shards=ShardInfo{total=2, successful=1, failures=[]
    }
]


5 Get Documents with pagination

curl -X GET \
  http://localhost:8080/index/search \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Content-Type: application/json' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: 89e159da-e2ed-4769-80a6-93c18afb965a,e3e7fa91-1998-4fc1-a18a-16945b8e1082' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache'
  
  
  {
    "took": 1,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 5,
            "relation": "eq"
        },
        "max_score": 1.0,
        "hits": [
            {
                "_index": "payment_transactions",
                "_type": "_doc",
                "_id": "0071584271839965",
                "_score": 1.0,
                "_source": {
                    "transaction_date": "2020-03-15T11:30:40.424Z",
                    "cvv": "007",
                    "transaction_origin": "mobile_androide",
                    "amount": "1200",
                    "card_number": "4214010103025487",
                    "merchant_name": "desifood",
                    "merchant_id": "007",
                    "transaction_type": "CNP",
                    "customer_email_id": "nameless@wonder.com",
                    "exp_dt": "0721",
                    "customer_name": "nameless wonder",
                    "shipping_address": "#32,Mars",
                    "cuurency_code": "IND"
                }
            },
            {
                "_index": "payment_transactions",
                "_type": "_doc",
                "_id": "0071584273161887",
                "_score": 1.0,
                "_source": {
                    "transaction_date": "2020-03-15T11:58:39.106Z",
                    "cvv": "007",
                    "transaction_origin": "mobile_androide",
                    "amount": "1300",
                    "card_number": "4214010103025487",
                    "merchant_name": "desifood",
                    "merchant_id": "007",
                    "transaction_type": "CNP",
                    "customer_email_id": "nameless@wonder.com",
                    "exp_dt": "0721",
                    "customer_name": "nameless wonder",
                    "shipping_address": "#32, Elong must colony. Tesla city. Mars",
                    "cuurency_code": "IND"
                }
            },
            {
                "_index": "payment_transactions",
                "_type": "_doc",
                "_id": "0081584274830207",
                "_score": 1.0,
                "_source": {
                    "transaction_date": "2020-03-15T12:20:30.454Z",
                    "cvv": "008",
                    "transaction_origin": "mobile_androide",
                    "amount": "1300",
                    "card_number": "4214010103025487",
                    "merchant_name": "idlidosastall",
                    "merchant_id": "008",
                    "transaction_type": "CNP",
                    "customer_email_id": "nameless@wonder.com",
                    "exp_dt": "0721",
                    "customer_name": "nameless wonder",
                    "shipping_address": "#32,Mars",
                    "cuurency_code": "IND"
                }
            },
            {
                "_index": "payment_transactions",
                "_type": "_doc",
                "_id": "0071584274870706",
                "_score": 1.0,
                "_source": {
                    "transaction_date": "2020-03-15T12:21:10.725Z",
                    "cvv": "007",
                    "transaction_origin": "mobile_androide",
                    "amount": "50",
                    "card_number": "4214010103025487",
                    "merchant_name": "desifood",
                    "merchant_id": "007",
                    "transaction_type": "CNP",
                    "customer_email_id": "nameless@wonder.com",
                    "exp_dt": "0721",
                    "customer_name": "nameless wonder",
                    "shipping_address": "#32,Mars",
                    "cuurency_code": "IND"
                }
            },
            {
                "_index": "payment_transactions",
                "_type": "_doc",
                "_id": "0071584274971764",
                "_score": 1.0,
                "_source": {
                    "transaction_date": "2020-03-15T12:22:51.784Z",
                    "cvv": "007",
                    "transaction_origin": "mobile_androide",
                    "amount": "50",
                    "card_number": "4214010103025487",
                    "merchant_name": "desifood",
                    "merchant_id": "007",
                    "transaction_type": "CNP",
                    "customer_email_id": "nameless@wonder.com",
                    "exp_dt": "0721",
                    "customer_name": "nameless wonder",
                    "shipping_address": "#32,Mars",
                    "cuurency_code": "IND"
                }
            }
        ]
    }
}


 6. Get by field
 
 curl -X GET \
  http://localhost:8080/index/merchant_id/008 \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: localhost:8080' \
  -H 'Postman-Token: cdc79dd4-d7f6-4334-8f5d-76f173cf327f,e06f3eb0-6768-4468-96f2-bb7df5a0d15d' \
  -H 'User-Agent: PostmanRuntime/7.16.3' \
  -H 'cache-control: no-cache'
  
  {
    "took": 0,
    "timed_out": false,
    "_shards": {
        "total": 1,
        "successful": 1,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": {
            "value": 1,
            "relation": "eq"
        },
        "max_score": 1.3862942,
        "hits": [
            {
                "_index": "payment_transactions",
                "_type": "_doc",
                "_id": "0081584274830207",
                "_score": 1.3862942,
                "_source": {
                    "transaction_date": "2020-03-15T12:20:30.454Z",
                    "cvv": "008",
                    "transaction_origin": "mobile_androide",
                    "amount": "1300",
                    "card_number": "4214010103025487",
                    "merchant_name": "idlidosastall",
                    "merchant_id": "008",
                    "transaction_type": "CNP",
                    "customer_email_id": "nameless@wonder.com",
                    "exp_dt": "0721",
                    "customer_name": "nameless wonder",
                    "shipping_address": "#32,Mars",
                    "cuurency_code": "IND"
                }
            }
        ]
    }
}
