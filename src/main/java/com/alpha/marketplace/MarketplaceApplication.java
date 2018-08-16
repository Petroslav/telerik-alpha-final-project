package com.alpha.marketplace;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.StreamUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.OutputStream;
import java.util.stream.Stream;

@SpringBootApplication
public class MarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketplaceApplication.class, args);
    }

    @Bean
    public CommandLineRunner houses(HouseRepository houseRepository, ApplicationContext context){
        return args -> {
            Resource fnpPic = context.getResource("https://cdn2.unrealengine.com/Fortnite%2Fblog%2FAndroid%2FBR05_Header_16_9_AndroidLaunch-1920x1080-7cab9f216f2f6f928f8d5d2394be157610e0638b.jpg");
            Resource idk = context.getResource("gs://telerik-alpha-final-project-marketplace/file.jpg");

            byte[] fileBytes = StreamUtils.copyToByteArray(fnpPic.getInputStream());

            try(OutputStream os = ((WritableResource) idk).getOutputStream()){
                os.write(fileBytes);
            }

            Stream.of(new House("wat1"),
                    new House("wat2"),
                    new House("wat3"))
                    .forEach(houseRepository::save);

            houseRepository.findAll()
                    .forEach(house -> System.out.println("New House: " + house.getAddress()));
        };
    }
}

interface HouseRepository extends CrudRepository<House, Long> {}

@Entity
class House {
    @Id
    @GeneratedValue
    private long id;
    private String address;

    House(String address) {
        this.address = address;
    }

    public House(){

    }

    String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
