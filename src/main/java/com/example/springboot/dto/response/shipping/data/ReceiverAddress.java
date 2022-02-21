package com.example.springboot.dto.response.shipping.data;

import java.util.List;

public class ReceiverAddress
{
    private String id;
    private String address_line;
    private String street_name;
    private String street_number;
    private String comment;
    private String zip_code;

    private City city;

    private State state;

    private Country country;

    private Neighborhood neighborhood;

    private Municipality municipality;

    private String agency;

    private List<String> types;

    private String latitude;
    private String longitude;
    private String geolocation_type;
    private String geolocation_last_updated;
    private String geolocation_source;
    private String receiver_name;
    private String receiver_phone;

    public ReceiverAddress(String id, String address_line, String street_name, String street_number, String comment, String zip_code, City city, State state, Country country, Neighborhood neighborhood, Municipality municipality, String agency, List<String> types, String latitude, String longitude, String geolocation_type, String geolocation_last_updated, String geolocation_source, String receiver_name, String receiver_phone) {
        this.id = id;
        this.address_line = address_line;
        this.street_name = street_name;
        this.street_number = street_number;
        this.comment = comment;
        this.zip_code = zip_code;
        this.city = city;
        this.state = state;
        this.country = country;
        this.neighborhood = neighborhood;
        this.municipality = municipality;
        this.agency = agency;
        this.types = types;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geolocation_type = geolocation_type;
        this.geolocation_last_updated = geolocation_last_updated;
        this.geolocation_source = geolocation_source;
        this.receiver_name = receiver_name;
        this.receiver_phone = receiver_phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress_line() {
        return address_line;
    }

    public void setAddress_line(String address_line) {
        this.address_line = address_line;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getStreet_number() {
        return street_number;
    }

    public void setStreet_number(String street_number) {
        this.street_number = street_number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Neighborhood getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getGeolocation_type() {
        return geolocation_type;
    }

    public void setGeolocation_type(String geolocation_type) {
        this.geolocation_type = geolocation_type;
    }

    public String getGeolocation_last_updated() {
        return geolocation_last_updated;
    }

    public void setGeolocation_last_updated(String geolocation_last_updated) {
        this.geolocation_last_updated = geolocation_last_updated;
    }

    public String getGeolocation_source() {
        return geolocation_source;
    }

    public void setGeolocation_source(String geolocation_source) {
        this.geolocation_source = geolocation_source;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }
}
