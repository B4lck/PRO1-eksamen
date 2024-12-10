console.log("Hello, World!");

// Burger menu
const burgerIcon = document.getElementById("burger-icon");
const navbar = document.getElementById("navbar");

burgerIcon.addEventListener("touchend", function() {
    setTimeout(() => {
        navbar.classList.toggle("openNavbar");
    }, 1);
})

document.body.addEventListener("touchend", function() {
    navbar.classList.remove("openNavbar");
})

