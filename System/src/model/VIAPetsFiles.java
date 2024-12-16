package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VIAPetsFiles {

    /**
     * Instans variabel over modellen der skal gemmes
     */
    private final VIAPetsModel model;
    /**
     * Instans variabel over stien, hvor filerne gemmes
     */
    private Path saveLocation = Paths.get(System.getProperty("user.home"), "VIAPets");

    /**
     * Konstruktør
     * @param model model der skal gemmes
     */
    public VIAPetsFiles(VIAPetsModel model) {
        this.model = model;
    }

    /**
     * Returnerer positionen hvor filerne gemmes
     * @return Path hvor filerne gemmes
     */
    public Path getSaveLocation() {
        return saveLocation;
    }

    /**
     * Sætter positionen hvor filerne gemmes
     * @param saveLocation Path til hvor filerne skal gemmes
     */
    public void setSaveLocation(Path saveLocation) {
        this.saveLocation = saveLocation;
    }

    /**
     * Gemmer alle listerne
     */
    public void saveAll() {
        saveAnimals();
        saveSales();
        saveReservations();
        saveCustomers();
        saveEmployees();
        saveAnimalsForPublic();
        saveReservationsForPublic();
    }

    /**
     * Loader alle listerne
     */
    public void loadAll() {
        loadAnimals();
        loadSales();
        loadReservations();
        loadCustomers();
        loadEmployees();
    }

    /**
     * Gemmer alle dyr
     */
    public void saveAnimals() {

        File saveFile = new File(saveLocation.toFile(), "animals.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Id;Kategori;Navn;Til Salg?;Pris;Ejer ID;Mad;Kommentar;Fødselsdagsdato;Oprettelsesdato;Billede URL\n");


        for (Animal animal : model.getAnimalList().getAllAnimals()) {
            serialized.append(escape(Integer.toString(animal.getAnimalId()))).append(";").append(escape(animal.getCategory())).append(";").append(escape(animal.getName())).append(";").append(escape(Boolean.toString(animal.isForSale()))).append(";").append(escape(animal.isForSale() ? Double.toString(animal.getPrice()) : "0")).append(";").append(escape(animal.isForSale() ? "-1" : Integer.toString(animal.getOwnerId()))).append(";").append(escape(animal.getFood())).append(";").append(escape(animal.getComment())).append(";").append(escape(animal.getBirthday().toString())).append(";").append(escape(animal.getCreationDate().toString())).append(";").append(escape(animal.getImageUrl())).append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    /**
     * Loader alle dyrene
     */
    public void loadAnimals() {
        File saveFile = new File(saveLocation.toFile(), "animals.csv");

        try {
            saveFile.setReadable(true);
            FileReader fr = new FileReader(saveFile);
            BufferedReader br = new BufferedReader(fr);

            model.getAnimalList().clear();

            // Første linje er ligegyldig
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(";", -1);

                int id = Integer.parseInt(unescape(data[0]));
                String category = unescape(data[1]);
                String name = unescape(data[2]);
                boolean isForSale = Boolean.parseBoolean(unescape(data[3]));
                double price = Double.parseDouble(unescape(data[4]));
                int ownerId = Integer.parseInt(unescape(data[5]));
                String food = unescape(data[6]);
                String comment = unescape(data[7]);
                Date birthday = new Date(unescape(data[8]));
                Date creationDate = new Date(unescape(data[9]));
                String imageUrl = unescape(data[10]);

                Animal animal;

                // Opret dyr til salg
                if (isForSale) animal = switch (category) {
                    case Animal.CATEGORY_BIRD -> new AnimalBird(name, price, id);
                    case Animal.CATEGORY_FISH -> new AnimalFish(name, price, id);
                    case Animal.CATEGORY_REPTILE -> new AnimalReptile(name, price, id);
                    default -> new Animal(name, price, id);
                };
                    // Opret dyr til pasning
                else animal = switch (category) {
                    case Animal.CATEGORY_BIRD -> new AnimalBird(name, ownerId, id);
                    case Animal.CATEGORY_FISH -> new AnimalFish(name, ownerId, id);
                    case Animal.CATEGORY_REPTILE -> new AnimalReptile(name, ownerId, id);
                    default -> new Animal(name, ownerId, id);
                };
                
                animal.setFood(food);
                animal.setComment(comment);
                animal.setBirthday(birthday);
                animal.setCreationDate(creationDate);
                animal.setImageUrl(imageUrl);
                
                model.getAnimalList().add(animal);
            }

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Gemmer alle salg
     */
    public void saveSales() {

        File saveFile = new File(saveLocation.toFile(), "sales.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Pris;Dyrets Id;Kundens Id;Medarbejderens Id;Dato for salg\n");


        for (Sale sale : model.getSalesList().getAllSales()) {
            serialized.append(escape(Double.toString(sale.getFinalPrice()))).append(";").append(escape(Integer.toString(sale.getAnimalId()))).append(";").append(escape(Integer.toString(sale.getCustomerId()))).append(";").append(escape(Integer.toString(sale.getEmployeeId()))).append(";").append(escape(sale.getDateOfSale().toString())).append(";").append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    /**
     * Loader alle salg
     */
    public void loadSales() {
        File saveFile = new File(saveLocation.toFile(), "sales.csv");

        try {
            saveFile.setReadable(true);
            FileReader fr = new FileReader(saveFile);
            BufferedReader br = new BufferedReader(fr);

            model.getSalesList().clear();

            // Første linje er ligegyldig
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(";", -1);

                double price = Double.parseDouble(unescape(data[0]));
                int animalId = Integer.parseInt(unescape(data[1]));
                int customerId = Integer.parseInt(unescape(data[2]));
                int employeeId = Integer.parseInt(unescape(data[3]));
                Date dateOfSale = new Date(unescape(data[4]));

                Sale sale = new Sale(price, animalId, customerId, employeeId, dateOfSale);
                
                model.getSalesList().add(sale);
            }

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Gemmer alle reservationerne
     */
    public void saveReservations() {

        File saveFile = new File(saveLocation.toFile(), "reservations.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Dyrets ID;Kundens ID;Startdato;Slutdato;Position\n");


        for (Reservation reservation : model.getReservationList().getAllReservations()) {
            serialized.append(escape(Integer.toString(reservation.getAnimalId()))).append(";").append(escape(Integer.toString(reservation.getCustomerId()))).append(";").append(escape(reservation.getPeriod().getStartDate().toString())).append(";").append(escape(reservation.getPeriod().getEndDate().toString())).append(";").append(escape(Integer.toString(reservation.getPositionId()))).append(";").append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    /**
     * Loader alle reservationerne
     */
    public void loadReservations() {
        File saveFile = new File(saveLocation.toFile(), "reservations.csv");

        try {
            saveFile.setReadable(true);
            FileReader fr = new FileReader(saveFile);
            BufferedReader br = new BufferedReader(fr);

            model.getReservationList().clear();

            // Første linje er ligegyldig
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(";", -1);

                int animalId = Integer.parseInt(unescape(data[0]));
                int customerId = Integer.parseInt(unescape(data[1]));
                Date startDate = new Date(unescape(data[2]));
                Date endDate = new Date(unescape(data[3]));
                int position = Integer.parseInt(unescape(data[4]));

                Reservation reservation = new Reservation(customerId, animalId, new DateInterval(startDate, endDate));
                
                reservation.setPositionId(position);

                model.getReservationList().add(reservation);
            }

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Gemmer alle kunderne
     */
    public void saveCustomers() {

        File saveFile = new File(saveLocation.toFile(), "customers.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Kundens ID;Navn;Telefon nr.;Email\n");

        for (Customer customer : model.getCustomerList().getAllCustomers()) {
            serialized.append(escape(Integer.toString(customer.getCustomerId()))).append(";").append(escape(customer.getName())).append(";").append(escape(Long.toString(customer.getPhone()))).append(";").append(escape(customer.getEmail())).append(";").append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    /**
     * Loader alle kunderne
     */
    public void loadCustomers() {
        File saveFile = new File(saveLocation.toFile(), "customers.csv");

        try {
            saveFile.setReadable(true);
            FileReader fr = new FileReader(saveFile);
            BufferedReader br = new BufferedReader(fr);

            model.getCustomerList().clear();

            // Første linje er ligegyldig
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(";", -1);

                int customerId = Integer.parseInt(unescape(data[0]));
                String name = unescape(data[1]);
                long phone = Long.parseLong(unescape(data[2]));
                String email = unescape(data[3]);

                Customer customer = new Customer(name, phone, email, customerId);

                model.getCustomerList().add(customer);
            }

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Gemmer alle medarbejderne
     */
    public void saveEmployees() {
        File saveFile = new File(saveLocation.toFile(), "employees.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Medarbejderens ID;Navn;Beskrivelse;Portrait URL\n");


        for (Employee employee : model.getEmployeeList().getAllEmployees()) {
            serialized.append(escape(Integer.toString(employee.getEmployeeId()))).append(";").append(escape(employee.getName())).append(";").append(escape(employee.getDescription())).append(";").append(escape(employee.getPortraitUrl())).append(";").append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    /**
     * Loader alle medarbejderne
     */
    public void loadEmployees() {
        File saveFile = new File(saveLocation.toFile(), "employees.csv");

        try {
            saveFile.setReadable(true);
            FileReader fr = new FileReader(saveFile);
            BufferedReader br = new BufferedReader(fr);

            model.getEmployeeList().clear();

            // Første linje er ligegyldig
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(";", -1);

                int employeeId = Integer.parseInt(unescape(data[0]));
                String name = unescape(data[1]);
                String description = unescape(data[2]);
                String imageUrl = unescape(data[3]);

                Employee employee = new Employee(name, employeeId);
                
                employee.setDescription(description);
                employee.setPortraitUrl(imageUrl);

                model.getEmployeeList().add(employee);
            }

        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Fejl");
            errorAlert.showAndWait();
        }
    }

    /**
     * Gem animals for offentligheden
     */
    public void saveAnimalsForPublic() {

        File saveFile = new File(saveLocation.toFile(), "animals_public.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Id;Kategori;Navn;Pris;Kommentar;Fødselsdagsdato;Billede URL\n");


        for (Animal animal : model.getAnimalList().getAnimalsForSale().getAllAnimals()) {
            serialized.append(escape(Integer.toString(animal.getAnimalId()))).append(";")
                    .append(escape(animal.getCategory())).append(";")
                    .append(escape(animal.getName())).append(";")
                    .append(escape(Integer.toString(animal.getOwnerId()))).append(";")
                    .append(escape(animal.getBirthday().toString())).append(";")
                    .append(escape(animal.getImageUrl())).append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    /**
     * Gem reservation for offentligheden
     */
    public void saveReservationsForPublic() {

        File saveFile = new File(saveLocation.toFile(), "reservations_public.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }

        StringBuilder serialized = new StringBuilder();

        serialized.append("Id;Kategori;Navn;Pris;Kommentar;Fødselsdagsdato;Billede URL\n");


        for (Reservation reservation : model.getReservationList().getAllReservations()) {
            serialized.append(escape(reservation.getPeriod().getStartDate().toString())).append(";")
                    .append(escape(reservation.getPeriod().getEndDate().toString())).append("\n");
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized.toString());
            fw.close();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            errorAlert.setGraphic(null);
            errorAlert.setHeaderText(null);
            errorAlert.setTitle("Kunne ikke gemme");
            errorAlert.showAndWait();
        }
    }

    private String escape(String str) {
        return str
                .replace("\n", "%0A")
                .replace(";", "%3B");
    }

    private String unescape(String str) {
        return str
                .replace("%0A", "\n")
                .replace("%3B", ";");
    }
}
