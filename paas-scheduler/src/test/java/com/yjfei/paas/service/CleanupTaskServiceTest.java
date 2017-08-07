package com.yjfei.paas.service;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.yjfei.paas.PaaSApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PaaSApplication.class)
@Transactional
@Rollback
public class CleanupTaskServiceTest {

}
