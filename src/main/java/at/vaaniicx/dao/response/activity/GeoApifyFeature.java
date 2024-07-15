package at.vaaniicx.dao.response.activity;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GeoApifyFeature {
    @JsonProperty
    private double latitude;

    @JsonProperty
    private double longitude;

    @JsonProperty
    private String name;

    @JsonProperty
    private String street;

    @JsonProperty("house_number")
    private String houseNumber;

    @JsonProperty("postcode")
    private String postCode;

    @JsonProperty
    private String city;

    @JsonProperty
    private String website;

    @JsonProperty
    private String[] categories;

    @JsonProperty("features")
    private void unpackFeatures(Map<String, Object>[] features) {
        Map<String, Object> properties = (Map<String, Object>) features[0].get("properties");
        this.latitude = Double.parseDouble(properties.get("lat").toString());
        this.longitude = Double.parseDouble(properties.get("lon").toString());
        this.street = properties.get("street").toString();
        this.houseNumber = properties.get("housenumber") == null ? null : properties.get("housenumber").toString();
        this.postCode = properties.get("postcode") == null ? null : properties.get("postcode").toString();
        this.city = properties.get("city").toString();
        this.categories = new ObjectMapper().convertValue(properties.get("categories"), String[].class);
    }
}
