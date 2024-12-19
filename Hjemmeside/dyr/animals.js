class Animal {
    id;
    category;
    name;
    price;
    comment;
    birthday;
    url;

    constructor(id, category, name, price, comment, birthday) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.price = price;
        this.comment = comment;
        this.birthday = birthday;
        this.url = "/img/kaninDyr.jpg"; // det er altid det samme billede
    }
}

/**
 * liste over dyr
 * @type {Animal[]}
 */
let animals = []
fetch("/data/animals_public.csv")
    .then(res => res.text())
    .then(data => {
        let rows = data.split("\n");
        for (let i = 1; i<rows.length; i++) {
            let cols = rows[i].split(";");
            if (cols.length > 1) {
                animals.push(new Animal(cols[0], cols[1], cols[2], cols[3], cols[4], parseDate(cols[5]), cols[6]));
            }
        }
        updateAnimals();
    })

function yearSince(date) {
    let today = new Date();
    let age = today.getFullYear() - (date.getFullYear() - 1); // getFullYear returnere åbenbart 2025, selvom der står 2024?!?!?!?
    if (today.getMonth() <= date.getMonth() && today.getDate() <= date.getDate()) age++;
    return age;
}

function parseDate(dateString) {
    let [d,m,y] = dateString.split("/").map(str => parseInt(str));
    return new Date(y,m-1,d);
}

function updateAnimals() {
    // Slet alle eksisterende rows
    let rows = document.getElementsByClassName("row");
    for (let r of rows) {
        r.remove();
    }

    // Tilføj dyr til siden
    let row;
    for (let i = 0; i < animals.length; i++) {
        if (i % 4 === 0) {
            row = document.createElement("div");
            row.classList.add("row");
            document.getElementById("container").appendChild(row);
        }

        let animal = animals[i];
        let animalObject = document.createElement("div");
        animalObject.classList.add("animal");
        animalObject.innerHTML = `
                    <img src='${animal.url}' alt='${animal.name}'>
                    <div class='left'>
                        <h2>${animal.name}</h2>
                        <h3>${yearSince(animal.birthday)} år</h3>
                        <p>${animal.price},-- kr.</p>
                    </div>
                    <div class='right'>
                        <p class='beforePrice'></p>
                        <p class='saveText'></p>
                    </div>
                    <button>Læs mere</button>`;

        row.appendChild(animalObject);
    }
    document.getElementById("container").appendChild(row);
}