// mapbox-config.js
var lng = -74.006; // Example longitude value
var lat = 40.7128; // Example latitude value

mapboxgl.accessToken = '';
var map = new mapboxgl.Map({
    container: 'map2', // container ID
    style: 'mapbox://styles/mapbox/streets-v11', // style URL
    center: [lng, lat], // starting position [lng, lat]
    zoom: 9 // starting zoom
});