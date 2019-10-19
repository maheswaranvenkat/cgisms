package com.cgi.sms.business.service.cucumber;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ComponentScan(basePackages = {"com.cgi.sms.business", "com.cgi.sms.dao", "com.cgi.sms.helper"})
public class StepsConfiguration {
}
