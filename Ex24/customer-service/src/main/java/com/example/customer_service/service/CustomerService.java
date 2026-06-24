package com.example.customer_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import com.example.customer_service.model.Customer;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "customer-data";

    public String createCustomer(Customer customer) {

        Map<String, AttributeValue> item = new HashMap<>();

        item.put("CustomerId",
                AttributeValue.builder()
                        .s(customer.getCustomerId())
                        .build());

        item.put("Name",
                AttributeValue.builder()
                        .s(customer.getName())
                        .build());

        PutItemRequest request =
                PutItemRequest.builder()
                        .tableName(TABLE_NAME)
                        .item(item)
                        .build();

        dynamoDbClient.putItem(request);

        return "Customer Created";
    }

    public Map<String, AttributeValue> getCustomer(String customerId) {

        Map<String, AttributeValue> key = new HashMap<>();

        key.put("CustomerId",
                AttributeValue.builder()
                        .s(customerId)
                        .build());

        GetItemRequest request =
                GetItemRequest.builder()
                        .tableName(TABLE_NAME)
                        .key(key)
                        .build();

        return dynamoDbClient.getItem(request).item();
    }

    public String updateCustomer(String customerId, String name) {

        Map<String, AttributeValue> key = new HashMap<>();

        key.put("CustomerId",
                AttributeValue.builder()
                        .s(customerId)
                        .build());

        Map<String, AttributeValue> values = new HashMap<>();

        values.put(":name",
                AttributeValue.builder()
                        .s(name)
                        .build());

        UpdateItemRequest request =
                UpdateItemRequest.builder()
                        .tableName(TABLE_NAME)
                        .key(key)
                        .updateExpression("SET #n = :name")
                        .expressionAttributeNames(
                                Map.of("#n", "Name"))
                        .expressionAttributeValues(values)
                        .build();

        dynamoDbClient.updateItem(request);

        return "Customer Updated";
    }
}