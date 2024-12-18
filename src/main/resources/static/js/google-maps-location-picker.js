// google-maps-location-picker.js
import config from './config.js';

function loadGoogleMapsApi(callback) {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${config.apiKey}&libraries=places&callback=${callback}`;
    script.async = true;
    script.defer = true;
    document.head.appendChild(script);
}

function initMap() {
    var map = new google.maps.Map(document.getElementById('map'), {
        center: { lat: 36.8, lng: 10.2 },
        zoom: 10
    });

    var marker = new google.maps.Marker({
        map: map,
        draggable: true
    });

    var input = document.getElementById('search-box');
    var searchBox = new google.maps.places.SearchBox(input);

    map.addListener('click', function (e) {
        placeMarkerAndPanTo(e.latLng, map);
    });

    searchBox.addListener('places_changed', function () {
        var places = searchBox.getPlaces();
        if (places.length == 0) {
            return;
        }

        var bounds = new google.maps.LatLngBounds();
        places.forEach(function (place) {
            if (!place.geometry) {
                console.log("Returned place contains no geometry");
                return;
            }

            if (place.geometry.viewport) {
                bounds.union(place.geometry.viewport);
            } else {
                bounds.extend(place.geometry.location);
            }

            placeMarkerAndPanTo(place.geometry.location, map);
        });
        map.fitBounds(bounds);
    });

    function placeMarkerAndPanTo(latLng, map) {
        marker.setPosition(latLng);
        map.panTo(latLng);
        document.getElementById('locationLatitude').value = latLng.lat();
        document.getElementById('locationLongitude').value = latLng.lng();
        fetchAddress(latLng);
    }

    function fetchAddress(latLng) {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({ 'location': latLng }, function (results, status) {
            if (status === 'OK') {
                if (results[0]) {
                    document.getElementById('locationAddress').value = results[0].formatted_address;
                    document.getElementById('locationName').value = results[0].name || 'No name found';
                    // Assuming phone and email are part of the address components
                    var phone = results[0].address_components.find(component => component.types.includes('phone')) || { long_name: 'No phone found' };
                    var email = results[0].address_components.find(component => component.types.includes('email')) || { long_name: 'No email found' };
                    document.getElementById('locationPhone').value = phone.long_name;
                    document.getElementById('locationEmail').value = email.long_name;
                } else {
                    document.getElementById('locationAddress').value = 'No address found';
                    document.getElementById('locationName').value = 'No name found';
                    document.getElementById('locationPhone').value = 'No phone found';
                    document.getElementById('locationEmail').value = 'No email found';
                }
            } else {
                document.getElementById('locationAddress').value = 'Geocoder failed due to: ' + status;
                document.getElementById('locationName').value = 'Geocoder failed due to: ' + status;
                document.getElementById('locationPhone').value = 'Geocoder failed due to: ' + status;
                document.getElementById('locationEmail').value = 'Geocoder failed due to: ' + status;
            }
        });
    }
}

document.addEventListener('DOMContentLoaded', function () {
    loadGoogleMapsApi('initMap');
});