package edu.uci.banerjee.burnserver;

import edu.uci.banerjee.burnserver.services.LandOwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@SpringBootTest
public class LandOwnershipServiceTest {
    @Autowired
    private LandOwnershipService landService;

    @Test
    public void getOwnershipFromCoordinateTest(){
        Double lat = 38.90;
        Double lon = -121.05;

        Assertions.assertThat(landService.getOwnershipFromCoordinate(lat,lon)).isNotEmpty();
        Assertions.assertThat(landService.getOwnershipFromCoordinate(lat,lon)).isEqualTo("Federal");
    }

    @Test
    public void getOwnershipNullTest(){
        try{
            landService.getOwnershipFromCoordinate(null,null);
        }catch(Exception e){
            Assertions.assertThat(e).isInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void getOwnershipNonCaliforniaCoordinatesTest(){
        Double lat = 40.73;
        Double lon = -73.93; //New York Co-ordinates

        Assertions.assertThat(landService.getOwnershipFromCoordinate(lat,lon)).isEqualTo("Private");
    }
}
