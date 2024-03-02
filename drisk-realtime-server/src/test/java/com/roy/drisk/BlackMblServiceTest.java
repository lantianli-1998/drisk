package com.roy.drisk;

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
        service.addMblBlack("13888888888","test");
//        service.addMblBlack("13888888889","test");
//        service.addMblBlack("13888888810","test");
//        service.addMblBlack("13888888811","test");
//        service.addMblBlack("13888888812","test");
//        service.addMblBlack("13888888813","test");
        System.out.println("black mbl added");
    }

    @Test
    public void queryBlackMbl(){
        final BlackMblService service = GeneralServiceFactory.getService(BlackMblService.class);
        System.out.println(service.isMblInBlack("13888888888"));
        System.out.println(service.isMblInBlack("13812341234"));
    }
}
