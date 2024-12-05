package model;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VIAPetsFiles {

    private VIAPetsModel model;
    private Path saveLocation = Paths.get(System.getProperty("user.home"), "VIAPets");

    public VIAPetsFiles(VIAPetsModel model) {
        this.model = model;
    }

    public Path getSaveLocation() {
        return saveLocation;
    }

    public void setSaveLocation(Path saveLocation) {
        this.saveLocation = saveLocation;
    }
    
    public void saveAll() {
        saveAnimals();
        saveSales();
        saveReservations();
        saveCustomers();
        saveEmployees();
    }
    
    public void loadAll() {
        loadAnimals();
        loadSales();
        loadReservations();
        loadCustomers();
        loadEmployees();
    }

    public void saveAnimals() {

        File saveFile = new File(saveLocation.toFile(), "animals.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serialized = "";

        serialized += "Id;Kategori;Navn;Til Salg?;Pris;Ejer ID;Mad;Kommentar;Fødselsdagsdato;Oprettelsesdato;Billede URL\n";


        for (Animal animal : model.getAnimalList().getAllAnimals()) {
            serialized += escape(Integer.toString(animal.getAnimalId())) + ";"
                    + escape(animal.getCategory()) + ";"
                    + escape(animal.getName()) + ";"
                    + escape(Boolean.toString(animal.isForSale())) + ";"
                    + escape(animal.isForSale() ? Double.toString(animal.getPrice()) : "0") + ";"
                    + escape(animal.isForSale() ? "-1" : Integer.toString(animal.getOwnerId())) + ";"
                    + escape(animal.getFood()) + ";"
                    + escape(animal.getComment()) + ";"
                    + escape(animal.getBirthday().toString()) + ";"
                    + escape(animal.getCreationDate().toString()) + ";"
                    + escape(animal.getImageUrl())
                    + "\n";
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    public void saveSales() {

        File saveFile = new File(saveLocation.toFile(), "sales.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serialized = "";

        serialized += "Pris;Dyrets Id;Kundens Id;Medarbejderens Id;Dato for salg\n";


        for (Sale sale : model.getSalesList().getAllSales()) {
            serialized += escape(Double.toString(sale.getFinalPrice())) + ";"
                    + escape(Integer.toString(sale.getAnimalId())) + ";"
                    + escape(Integer.toString(sale.getCustomerId())) + ";"
                    + escape(Integer.toString(sale.getEmployeeId())) + ";"
                    + escape(sale.getDateOfSale().toString()) + ";"
                    + "\n";
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    public void saveReservations() {

        File saveFile = new File(saveLocation.toFile(), "reservations.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serialized = "";

        serialized += "Dyrets ID;Kundens ID;Startdato;Slutdato;Position\n";


        for (Reservation reservation : model.getReservationList().getAllReservations()) {
            serialized += escape(Integer.toString(reservation.getAnimalId())) + ";"
                    + escape(Integer.toString(reservation.getCustomerId())) + ";"
                    + escape(reservation.getPeriod().getStartDate().toString()) + ";"
                    + escape(reservation.getPeriod().getEndDate().toString()) + ";"
                    + escape(Integer.toString(reservation.getPositionId())) + ";"
                    + "\n";
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

                Reservation reservation = new Reservation(animalId, customerId, new DateInterval(startDate, endDate));
                
                reservation.setPositionId(position);

                model.getReservationList().add(reservation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomers() {

        File saveFile = new File(saveLocation.toFile(), "customers.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serialized = "";

        serialized += "Kundens ID;Navn;Telefon nr.;Email\n";

        for (Customer customer : model.getCustomerList().getAllCustomers()) {
            serialized += escape(Integer.toString(customer.getCustomerId())) + ";"
                    + escape(customer.getName()) + ";"
                    + escape(Long.toString(customer.getPhone())) + ";"
                    + escape(customer.getEmail()) + ";"
                    + "\n";
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
        }
    }

    public void saveEmployees() {

        File saveFile = new File(saveLocation.toFile(), "employees.csv");

        try {
            saveFile.setWritable(true);
            new File(saveLocation.toString()).mkdirs();
            saveFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String serialized = "";

        serialized += "Medarbejderens ID;Navn;Beskrivelse;Portrait URL\n";


        for (Employee employee : model.getEmployeeList().getAllEmployees()) {
            serialized += escape(Integer.toString(employee.getEmployeeId())) + ";"
                    + escape(employee.getName()) + ";"
                    + escape(employee.getDescription()) + ";"
                    + escape(employee.getPortraitUrl()) + ";"
                    + "\n";
        }

        try {
            FileWriter fw = new FileWriter(saveFile);
            fw.flush();
            fw.write(serialized);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
            e.printStackTrace();
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
