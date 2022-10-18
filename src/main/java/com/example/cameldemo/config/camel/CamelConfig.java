package com.example.cameldemo.config.camel;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CamelConfig extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:start")
                .bean("randomCitiesImpl", "getCities")
                .split(simple("${body}"))
                .parallelProcessing()
                .bean("chuckNorrisFactImpl", "getChuckFact")
                .transform().simple("${bean:chuckNorrisIOImpl?method=getJoke}.value")
                .aggregate(constant(true), batchAggregationStrategy())
                .completionSize(exchangeProperty(Exchange.SPLIT_SIZE))
                .log(LoggingLevel.INFO, "Message received : ${body}")
                .end();
    }

    @Bean
    private AggregationStrategy batchAggregationStrategy() {
        return new ArrayListAggregationStrategy();
    }

    private static class ArrayListAggregationStrategy implements AggregationStrategy {
        public ArrayListAggregationStrategy() {
            super();
        }
        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            Message newIn = newExchange.getIn();
            Object newBody = newIn.getBody();
            ArrayList list;
            if (oldExchange == null) {
                list = new ArrayList();
                list.add(newBody);
                newIn.setBody(list);
                return newExchange;
            } else {
                Message in = oldExchange.getIn();
                list = in.getBody(ArrayList.class);
                list.add(newBody);
                return oldExchange;
            }
        }
    }
}
