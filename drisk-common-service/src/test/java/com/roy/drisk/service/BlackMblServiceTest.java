package com.roy.drisk.service;

import com.roy.drisk.services.GeneralServiceFactory;
import com.roy.drisk.services.blackMbl.BlackMblService;
import org.junit.Before;
import org.junit.Test;

/**
 * @author roy
 * @date 2021/11/7
 * @desc
 */
public class BlackMblServiceTest {

    @Before
    public void before(){
        System.setProperty("drisk.env","sit");
    }

    @Test
    public void addBlackMbl(){
        final BlackMblService service = GeneralServiceFactory.getService(BlackMblService.class);
        service.addMblBlack("13874869527","test");
        System.out.println("black mbl added");
    }

    @Test
    public void queryBlackMbl(){
        final BlackMblService service = GeneralServiceFactory.getService(BlackMblService.class);
        System.out.println(service.isMblInBlack("13874869527"));
        System.out.println(service.isMblInBlack("13887654321"));
    }
}
