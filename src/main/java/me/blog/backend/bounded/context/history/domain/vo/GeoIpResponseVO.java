package me.blog.backend.bounded.context.history.domain.vo;

public record GeoIpResponseVO(String query, String status, String continent, String continentCode, String country, String countryCode, String region,
                            String regionName, String city, String district, String zip, String timezone, String isp, String mobile, String proxy,
                            String hosting, String lat, String lon) {}
