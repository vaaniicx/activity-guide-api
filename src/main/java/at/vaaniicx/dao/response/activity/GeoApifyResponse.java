package at.vaaniicx.dao.response.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class GeoApifyResponse {

    private GeoApifyFeature[] places;

    @JsonProperty("features")
    private void unpackFeatures(Map<String, Object>[] features) {
        this.places = new GeoApifyFeature[features.length];

        int count = 0;
        for (Map<String, Object> feature : features) {
            Map<String, Object> properties = (Map<String, Object>) feature.get("properties");

            String name = properties.get("name") == null ? null : properties.get("name").toString();
            double latitude = Double.parseDouble(properties.get("lat").toString());
            double longitude = Double.parseDouble(properties.get("lon").toString());
            String street = properties.get("street") == null ? null : properties.get("street").toString();
            String houseNumber = properties.get("housenumber") == null ? null : properties.get("housenumber").toString();
            String postCode = properties.get("postcode") == null ? null : properties.get("postcode").toString();
            String city = properties.get("city") == null ? null : properties.get("city").toString();
            String website = properties.get("website") == null ? null : properties.get("website").toString();
            String[] categories = new ObjectMapper().convertValue(properties.get("categories"), String[].class);

            this.places[count++] = new GeoApifyFeature(latitude, longitude, name, street, houseNumber, postCode, city,website, categories);
        }
    }
}
