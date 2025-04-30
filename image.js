document.addEventListener("DOMContentLoaded", function() {
    console.log("DOM fully loaded and parsed");

    try {
        document.body.style.backgroundImage = "url('/assets/bigcat.png')";
        console.log("Set background image");

        document.body.style.backgroundSize = "cover";
        console.log("Set background size to cover");

        document.body.style.backgroundPosition = "center";
        console.log("Set background position to center");

        document.body.style.backgroundRepeat = "no-repeat";
        console.log("Set background repeat to no-repeat");

        document.body.style.backgroundAttachment = "fixed";
        console.log("Set background attachment to fixed");
    } catch (error) {
        console.error("Error setting background image: ", error);
    }
});