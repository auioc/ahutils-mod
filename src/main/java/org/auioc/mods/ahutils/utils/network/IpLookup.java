package org.auioc.mods.ahutils.utils.network;

import static org.auioc.mods.ahutils.AHUtils.LOGGER;
import java.util.List;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.Marker;
import org.auioc.mods.ahutils.api.exception.HSimpleException;
import org.auioc.mods.ahutils.api.function.BiFunctionEx;
import org.auioc.mods.ahutils.common.config.CommonConfig;
import org.auioc.mods.ahutils.utils.LogUtil;

public interface IpLookup {

    static Marker marker = LogUtil.getMarker("IpLookup");

    static String GeoIP2ApiUrl = CommonConfig.GeoIP2ApiUrl.get();
    static List<? extends String> availableGeoip2Databases = CommonConfig.GeoIP2AvailableDatabases.get();

    static <T> T geoip2(String ip, String database, boolean simpleResult, BiFunctionEx<JsonObject, Boolean, T> handler) throws Exception {
        try {
            if (!availableGeoip2Databases.contains(database)) {
                throw new Exception("Database " + database + " of API " + GeoIP2ApiUrl + " is unavailable.");
            }
            JsonObject json = HttpUtils.getJson(GeoIP2ApiUrl + database + "/" + ip).getAsJsonObject();
            T result = handler.apply(json, simpleResult);
            return result;
        } catch (HSimpleException e) {
            LOGGER.error(marker, "Lookup IP location failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error(marker, "Lookup IP location failed: ", e);
            throw e;
        }
    }

    static String lookupAsn(String ip, boolean asnInfoOnly) throws Exception {
        return geoip2(
            ip, "asn", asnInfoOnly,
            (json, simpleResult) -> {
                if (simpleResult) {
                    return json.get("autonomous_system_number").getAsString() + " " + json.get("autonomous_system_organization").getAsString();
                } else {
                    return json.toString();
                }
            }
        );
    }

    static String lookupCountry(String ip, boolean isoCodeOnly) throws Exception {
        return geoip2(
            ip, "country", isoCodeOnly,
            (json, simpleResult) -> {
                if (isoCodeOnly) {
                    return json.getAsJsonObject("country").get("iso_code").getAsString();
                } else {
                    return json.toString();
                }
            }
        );
    }

    static String lookupCity(String ip, boolean cityNameOnly) throws Exception {
        return geoip2(
            ip, "country", cityNameOnly,
            (json, simpleResult) -> {
                if (cityNameOnly) {
                    JsonObject city = json.getAsJsonObject("city");
                    if (city != null) {
                        return city.getAsJsonObject("names").get("en").getAsString();
                    } else {
                        throw new HSimpleException("Unable to determine the city of the ip " + ip + ", please try asn or country instead.");
                    }
                } else {
                    return json.toString();
                }
            }
        );
    }

}
