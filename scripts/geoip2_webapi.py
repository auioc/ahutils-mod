import os
import sys

import geoip2.database
from flask import Flask, jsonify

os.chdir(os.path.abspath(os.path.dirname(sys.argv[0])))

ASN_DB = geoip2.database.Reader("geoip2\GeoLite2-ASN.mmdb")
COUNTRY_DB = geoip2.database.Reader("geoip2\GeoLite2-Country.mmdb")
CITY_DB = geoip2.database.Reader("geoip2\GeoLite2-City.mmdb")

FLASK = Flask(__name__)
FLASK.config["JSON_AS_ASCII"] = False


def query(mode: int, ip: str):
    try:
        match mode:
            case 1:
                return jsonify(ASN_DB.asn(ip).raw)
            case 2:
                return jsonify(COUNTRY_DB.country(ip).raw)
            case 3:
                return jsonify(CITY_DB.city(ip).raw)
    except Exception as e:
        return jsonify({"message": str(e)}), 500


@FLASK.route("/geoip2/asn/<string:ip>", methods=["GET"])
def queryAsn(ip):
    return query(1, ip)


@FLASK.route("/geoip2/country/<string:ip>", methods=["GET"])
def queryCountry(ip):
    return query(2, ip)


@FLASK.route("/geoip2/city/<string:ip>", methods=["GET"])
def queryCity(ip):
    return query(3, ip)


if __name__ == "__main__":
    try:
        FLASK.run(host="127.0.0.1", port=80)
    except Exception as e:
        print(e)
    finally:
        ASN_DB.close()
        COUNTRY_DB.close()
        CITY_DB.close()
