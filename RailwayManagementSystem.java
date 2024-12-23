import java.util.*;

public class RailwayManagementSystem {

    private static HashMap<String, String> users = new HashMap<>();
    private static HashMap<String, String> admins = new HashMap<>();
    private static ArrayList<Train> trains = new ArrayList<>();
    private static HashMap<String, List<Train>> userBookings = new HashMap<>();
    private static final String[] jiangsuCities = {
        "Nanjing", "Suzhou", "Wuxi", "Changzhou", "Zhenjiang", "Yangzhou", "Xuzhou", 
        "Lianyungang", "Huai'an", "Yancheng", "Taizhou", "Kunshan", "Jiangyin", 
        "Changshu", "Yangzhou", "Nantong", "Lianyungang", "Luqiao", "Qidong"
    };
    private static TreeMap<String, List<Train>> sortedTrainsBySource = new TreeMap<>();

    public static void main(String[] args) {
        preloadData();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to Jiangsu Railway System");
            System.out.println("1. User Login\n2. Admin Login\n3. Exit");
            int choice = getValidIntegerInput(scanner, "Enter your choice: ");

            switch (choice) {
                case 1:
                    userLogin(scanner);
                    break;
                case 2:
                    adminLogin(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void preloadData() {
        users.put("user1", "password1");
        users.put("user2", "password2");

        admins.put("admin", "admin123");

        // Add Jiangsu train data to the TreeMap
        String[] times = {"00:00", "03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00"};

        int trainId = 101;
        for (int i = 0; i < 30; i++) {
            String source = jiangsuCities[i % jiangsuCities.length];
            String destination = jiangsuCities[(i + 1) % jiangsuCities.length];
            String departure = times[i % times.length];
            String arrival = times[(i + 1) % times.length];
            
            // Generate intermediate stations
            List<String> stations = getStationsBetween(source, destination);

            Train train = new Train(trainId++, "Train" + i, source, destination, departure, arrival, 50.0 + (i * 10), stations);
            trains.add(train);

            // Adding to sortedTrainsBySource TreeMap
            sortedTrainsBySource.putIfAbsent(source, new ArrayList<>());
            sortedTrainsBySource.get(source).add(train);
        }
    }

    // Method to get all intermediate stations between source and destination
    private static List<String> getStationsBetween(String source, String destination) {
        List<String> stations = new ArrayList<>();
        boolean addStations = false;
        for (String city : jiangsuCities) {
            if (city.equals(source)) {
                addStations = true;  // Start adding stations after the source
            }
            if (addStations) {
                stations.add(city);
            }
            if (city.equals(destination)) {
                break;  // Stop adding stations after the destination
            }
        }
        return stations;
    }
    private static void displayTrainsWithStations() {
        System.out.println("List of all trains with intermediate stations:");
        for (Train train : trains) {
            System.out.println(train);
        }
    }

    // Method to get a valid integer input
    private static int getValidIntegerInput(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine();  // Consume newline
                break;
            } else {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.nextLine();  // Clear invalid input
            }
        }
        return input;
    }

    private static void userLogin(Scanner scanner) {
        while (true) {
            System.out.print("Enter User ID: ");
            String userId = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();

            if (users.containsKey(userId) && users.get(userId).equals(password)) {
                System.out.println("User login successful! Welcome " + userId);
                userMenu(scanner, userId);
                break;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    private static void adminLogin(Scanner scanner) {
        while (true) {
            System.out.print("Enter Admin ID: ");
            String adminId = scanner.next();
            System.out.print("Enter Password: ");
            String password = scanner.next();

            if (admins.containsKey(adminId) && admins.get(adminId).equals(password)) {
                System.out.println("Admin login successful! Welcome " + adminId);
                adminMenu(scanner);
                break;
            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    private static void userMenu(Scanner scanner, String userId) {
        while (true) {
            System.out.println("\nUser Menu");
            System.out.println("1. View Trains");
            System.out.println("2. Search Train by Destination");
            System.out.println("3. Search Train by Departure Station");
            System.out.println("4. Search Train by Time");
            System.out.println("5. Search Train by Departure and Destination");
            System.out.println("6. Book Train");
            System.out.println("7. Refund Ticket");
            System.out.println("8. View Past Bookings");  // New option for viewing past bookings
            System.out.println("9. Logout");
    
            int choice = getValidIntegerInput(scanner, "Enter your choice: ");
            switch (choice) {
                case 1:
                    viewTrains();
                    break;
                case 2:
                    searchTrainByDestination(scanner);
                    break;
                case 3:
                    searchTrainByDepartureStation(scanner);
                    break;
                case 4:
                    searchTrainByTime(scanner);
                    break;
                case 5:
                    searchTrainByDepartureAndDestination(scanner);
                    break;
                case 6:
                    bookTrain(scanner, userId);
                    break;
                case 7:
                    refundTicket(scanner, userId);
                    break;
                case 8:
                    viewPastBookings(userId);  // Display the past bookings when the option is selected
                    break;
                case 9:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void adminMenu(Scanner scanner) {
        while (true) {
            System.out.println("\nAdmin Menu");
            System.out.println("1. View Trains\n2. Modify Train Time\n3. Delay Train\n4. Add New Train\n5. Logout");
            int choice = getValidIntegerInput(scanner, "Enter your choice: ");
    
            switch (choice) {
                case 1:
                    viewTrains();
                    break;
                case 2:
                    modifyTrainTime(scanner);
                    break;
                case 3:
                    delayTrain(scanner);
                    break;
                case 4:
                    addTrain(scanner);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    

    private static void viewTrains() {
        if (trains.isEmpty()) {
            System.out.println("No trains available.");
        } else {
            System.out.println("\nAvailable Trains:");
            for (Train train : trains) {
                System.out.println(train);
            }
        }
    }
    
    private static void addTrain(Scanner scanner) {
        System.out.println("Enter the details for the new train:");
    
        System.out.print("Train Name: ");
        scanner.nextLine(); // Consume leftover newline
        String name = scanner.nextLine(); 
    
        System.out.print("Source Station: ");
        String source = scanner.nextLine();
    
        System.out.print("Destination Station: ");
        String destination = scanner.nextLine();
    
        System.out.print("Departure Time (HH:mm): ");
        String departureTime = scanner.nextLine();
    
        System.out.print("Arrival Time (HH:mm): ");
        String arrivalTime = scanner.nextLine();
    
        System.out.print("Train Price: ");
        double price = getValidDoubleInput(scanner, "Enter a valid price: ");
    
        // Generate a new train ID
        int newTrainId = trains.size() + 101;
    
        // Generate the list of intermediate stations between the source and destination
        List<String> stations = getIntermediateStations(source, destination);
    
        // Create and add the new train to the list using the new constructor
        Train newTrain = new Train(newTrainId, name, source, destination, departureTime, arrivalTime, price, stations);
        trains.add(newTrain);
    
        System.out.println("New train added successfully!");
        System.out.println("Train Details: " + newTrain);
    }
    private static List<String> getIntermediateStations(String source, String destination) {
        List<String> stations = new ArrayList<>();
        int startIndex = Arrays.asList(jiangsuCities).indexOf(source);
        int endIndex = Arrays.asList(jiangsuCities).indexOf(destination);
    
        if (startIndex < endIndex) {
            for (int i = startIndex + 1; i <= endIndex; i++) {
                stations.add(jiangsuCities[i]);
            }
        } else {
            for (int i = startIndex - 1; i >= endIndex; i--) {
                stations.add(jiangsuCities[i]);
            }
        }
        return stations;
    }
        
    private static double getValidDoubleInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                return scanner.nextDouble();
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.next(); // Clear invalid input
            }
        }
    }
    
    
    private static void searchTrainByDestination(Scanner scanner) {
        System.out.print("Enter destination to search: ");
        String destination = scanner.next();
    
        System.out.println("Trains to " + destination + ":");
        boolean found = false;
        for (Train train : trains) {
            if (train.getDestination().equalsIgnoreCase(destination)) {
                System.out.println(train);
                System.out.println("Intermediate stations: " + String.join(" -> ", train.getStations())); // Show intermediate stations
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No trains found for the destination: " + destination);
        }
    }
    

    private static void searchTrainByDepartureStation(Scanner scanner) {
        System.out.print("Enter departure station to search: ");
        String departureStation = scanner.next();
    
        System.out.println("Trains departing from " + departureStation + ":");
        boolean found = false;
        if (sortedTrainsBySource.containsKey(departureStation)) {
            for (Train train : sortedTrainsBySource.get(departureStation)) {
                System.out.println(train);
                System.out.println("Intermediate stations: " + String.join(" -> ", train.getStations())); // Show intermediate stations
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No trains found departing from: " + departureStation);
        }
    }
    

    private static void searchTrainByTime(Scanner scanner) {
        System.out.print("Enter departure time to search (HH:mm): ");
        String time = scanner.next();
    
        System.out.println("Trains departing at " + time + ":");
        boolean found = false;
        for (Train train : trains) {
            if (train.getDepartureTime().equals(time)) {
                System.out.println(train);
                System.out.println("Intermediate stations: " + String.join(" -> ", train.getStations())); // Show intermediate stations
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No trains found departing at: " + time);
        }
    }
    

    private static void searchTrainByDepartureAndDestination(Scanner scanner) {
        System.out.print("Enter departure station: ");
        String departureStation = scanner.next();
        System.out.print("Enter destination station: ");
        String destinationStation = scanner.next();
    
        System.out.println("Trains from " + departureStation + " to " + destinationStation + ":");
        boolean found = false;
        if (sortedTrainsBySource.containsKey(departureStation)) {
            for (Train train : sortedTrainsBySource.get(departureStation)) {
                if (train.getDestination().equalsIgnoreCase(destinationStation)) {
                    System.out.println(train);
                    System.out.println("Intermediate stations: " + String.join(" -> ", train.getStations())); // Show intermediate stations
                    found = true;
                }
            }
        }
    
        if (!found) {
            System.out.println("No trains found from " + departureStation + " to " + destinationStation);
        }
    }
    


    private static void bookTrain(Scanner scanner, String userId) {
        System.out.println("Enter the Train ID you want to book: ");
        int trainId = getValidIntegerInput(scanner, "Train ID: ");
        Train selectedTrain = null;
    
        // Search for the selected train by ID
        for (Train train : trains) {
            if (train.getTrainId() == trainId) {
                selectedTrain = train;
                break;
            }
        }
    
        if (selectedTrain == null) {
            System.out.println("Invalid Train ID. Please try again.");
            return;
        }
    
        System.out.print("Enter your name: ");
        String name = scanner.next();
    
        // Get the list of transit stations along the route
        List<String> transits = getTransits(selectedTrain.getSource(), selectedTrain.getDestination());
    
        // Calculate the adjusted price based on the number of transit stations
        double adjustedPrice = selectedTrain.getPrice() + (transits.size() * 5.0);  // Assuming $5 per transit
    
        // Display train details including transit stations and adjusted price
        System.out.println("Train Details: ");
        System.out.println("Source: " + selectedTrain.getSource());
        System.out.println("Destination: " + selectedTrain.getDestination());
        System.out.println("Departure Time: " + selectedTrain.getDepartureTime());
        System.out.println("Arrival Time: " + selectedTrain.getArrivalTime());
        System.out.println("Number of Transits: " + transits.size());
        System.out.println("Transits: " + String.join(", ", transits));
        System.out.println("Base Price: $" + selectedTrain.getPrice());
        System.out.println("Adjusted Price due to Transits: $" + adjustedPrice);
    
        // Handle payment selection and validation
        System.out.println("Select payment method:");
        System.out.println("1. Credit Card\n2. PayPal");
        int paymentChoice = getValidIntegerInput(scanner, "Choose payment method (1 or 2): ");
        String paymentMethod = "";
        String paymentDetails = "";
    
        // Credit Card option
        if (paymentChoice == 1) {
            paymentMethod = "Credit Card";
            System.out.print("Enter your credit card number: ");
            paymentDetails = scanner.next();
            
            // Validate Credit Card using a helper function
            while (!isValidCreditCard(paymentDetails)) {
                System.out.println("Invalid credit card number. Please enter a valid card (16 digits starting with 4 for Visa or 5 for MasterCard).");
                paymentDetails = scanner.next();
            }
        } 
        // PayPal option
        else if (paymentChoice == 2) {
            paymentMethod = "PayPal";
            System.out.print("Enter your PayPal account: ");
            paymentDetails = scanner.next();
    
            // Validate PayPal account using a helper function
            while (!isValidPayPalAccount(paymentDetails)) {
                System.out.println("Invalid PayPal account. Please enter a valid email address.");
                paymentDetails = scanner.next();
            }
        } 
        // Invalid payment choice
        else {
            System.out.println("Invalid payment choice. Booking failed.");
            return;
        }
    
        // Display booking details
        System.out.println("\nBooking Details:");
        System.out.println("Train: " + selectedTrain);
        System.out.println("Payment Method: " + paymentMethod + " (" + paymentDetails + ")");
        System.out.println("Total Price: $" + adjustedPrice);
    
        // Ask for booking confirmation
        System.out.print("Are you sure you want to book this train? (yes/no): ");
        String confirmation = scanner.next();
    
        // If confirmed, proceed with booking
        if (confirmation.equalsIgnoreCase("yes")) {
            // Set the bookedBy field to the user's name
            selectedTrain.setBookedBy(name);
    
            // Add the new booking to the user's list of bookings
            userBookings.putIfAbsent(userId, new ArrayList<>()); // Ensure the user has a list
            userBookings.get(userId).add(selectedTrain); // Add the new train to the user's list of bookings
    
            System.out.println("Ticket booked successfully!");
            System.out.println("Train: " + selectedTrain);
            System.out.println("Payment Method: " + paymentMethod + " (" + paymentDetails + ")");
            System.out.println("Total Price: $" + adjustedPrice);
        } else {
            System.out.println("Booking cancelled.");
        }
    }
    
    // Helper function to validate credit card number
    private static boolean isValidCreditCard(String cardNumber) {
        // Credit card number should be 16 digits and start with 4 (Visa) or 5 (MasterCard)
        return cardNumber.matches("^(4\\d{15}|5\\d{15})$");
    }
    
    // Helper function to validate PayPal account (email format validation)
    private static boolean isValidPayPalAccount(String paypalAccount) {
        // Basic email format validation
        return paypalAccount.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    private static List<String> getTransits(String source, String destination) {
        List<String> transits = new ArrayList<>();
        int sourceIndex = -1;
        int destinationIndex = -1;
    
        // Find the indices of the source and destination in the Jiangsu cities array
        for (int i = 0; i < jiangsuCities.length; i++) {
            if (jiangsuCities[i].equalsIgnoreCase(source)) {
                sourceIndex = i;
            }
            if (jiangsuCities[i].equalsIgnoreCase(destination)) {
                destinationIndex = i;
            }
        }
    
        // If source or destination is not found, return empty list
        if (sourceIndex == -1 || destinationIndex == -1) {
            return transits;
        }
    
        // Ensure source is before destination
        if (sourceIndex > destinationIndex) {
            int temp = sourceIndex;
            sourceIndex = destinationIndex;
            destinationIndex = temp;
        }
    
        // Add intermediate cities (transits) between source and destination
        for (int i = sourceIndex + 1; i < destinationIndex; i++) {
            transits.add(jiangsuCities[i]);
        }
    
        return transits;
    }
    // Function to display the user's past bookings
private static void viewPastBookings(String userId) {
    if (!userBookings.containsKey(userId) || userBookings.get(userId).isEmpty()) {
        System.out.println("You have no past bookings.");
        return;
    }

    System.out.println("Your Past Bookings:");
    for (Train bookedTrain : userBookings.get(userId)) {
        // Calculate transits for each booked train
        List<String> transits = getTransits(bookedTrain.getSource(), bookedTrain.getDestination());
        double adjustedPrice = bookedTrain.getPrice() + (transits.size() * 5.0); // Assuming $5 per transit

        // Display the booking details using the displayBookingDetails method
        displayBookingDetails(bookedTrain, transits, adjustedPrice, "", "");
    }
}

// Example of the displayBookingDetails method
private static void displayBookingDetails(Train selectedTrain, List<String> transits, double adjustedPrice, String paymentMethod, String paymentDetails) {
    System.out.println("Train Details: ");
    System.out.println("Source: " + selectedTrain.getSource());
    System.out.println("Destination: " + selectedTrain.getDestination());
    System.out.println("Departure Time: " + selectedTrain.getDepartureTime());
    System.out.println("Arrival Time: " + selectedTrain.getArrivalTime());
    System.out.println("Number of Transits: " + transits.size());

    if (transits.isEmpty()) {
        System.out.println("No intermediate stations.");
    } else {
        System.out.println("Transits: " + String.join(", ", transits));
    }

    System.out.println("Base Price: $" + selectedTrain.getPrice());
    System.out.println("Adjusted Price due to Transits: $" + adjustedPrice);
    System.out.println("Payment Method: " + paymentMethod + " (" + paymentDetails + ")");
    System.out.println("Total Price: $" + adjustedPrice);
}

// Helper function to get transits for a train based on the source and destination

    
    private static void refundTicket(Scanner scanner, String userId) {
        if (!userBookings.containsKey(userId) || userBookings.get(userId).isEmpty()) {
            System.out.println("No active bookings found. Unable to refund.");
            return;
        }
    
        // Display all bookings with the name of the person who booked the train
        System.out.println("Your booked trains:");
        List<Train> bookings = userBookings.get(userId);
        for (int i = 0; i < bookings.size(); i++) {
            Train bookedTrain = bookings.get(i);
            System.out.println((i + 1) + ". " + bookedTrain + (bookedTrain.getBookedBy() != null ? ", Booked by: " + bookedTrain.getBookedBy() : ""));
        }
    
        // Allow the user to select a booking to refund
        System.out.print("Enter the number of the booking you want to refund (1-" + bookings.size() + "): ");
        int bookingChoice = getValidIntegerInput(scanner, "Choice: ");
    
        if (bookingChoice < 1 || bookingChoice > bookings.size()) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }
    
        Train bookedTrain = bookings.get(bookingChoice - 1);
        System.out.print("Do you want to proceed with the refund for the train " + bookedTrain + "? (yes/no): ");
        String confirmation = scanner.next();
    
        if (confirmation.equalsIgnoreCase("yes")) {
            bookings.remove(bookingChoice - 1); // Remove the selected booking
            System.out.println("Ticket refunded successfully, you will receive refund amount in your account with 5% Deduction.");
        } else {
            System.out.println("Refund process canceled.");
        }
    }
    
    private static void modifyTrainTime(Scanner scanner) {
        System.out.print("Enter Train ID to modify: ");
        int trainId = getValidIntegerInput(scanner, "Train ID: ");
        
        Train selectedTrain = null;
        for (Train train : trains) {
            if (train.getTrainId() == trainId) {
                selectedTrain = train;
                break;
            }
        }

        if (selectedTrain == null) {
            System.out.println("Invalid Train ID. Please try again.");
            return;
        }

        System.out.print("Enter new departure time (HH:mm): ");
        String newDepartureTime = scanner.next();
        System.out.print("Enter new arrival time (HH:mm): ");
        String newArrivalTime = scanner.next();

        selectedTrain.setDepartureTime(newDepartureTime);
        selectedTrain.setArrivalTime(newArrivalTime);

        System.out.println("Train times updated successfully.");
        System.out.println("Updated Train Details: " + selectedTrain);
    }

    private static void delayTrain(Scanner scanner) {
        System.out.print("Enter Train ID to delay: ");
        int trainId = getValidIntegerInput(scanner, "Train ID: ");
        
        Train selectedTrain = null;
        for (Train train : trains) {
            if (train.getTrainId() == trainId) {
                selectedTrain = train;
                break;
            }
        }

        if (selectedTrain == null) {
            System.out.println("Invalid Train ID. Please try again.");
            return;
        }

        System.out.print("Enter delay duration in minutes: ");
        int delayMinutes = getValidIntegerInput(scanner, "Delay (in minutes): ");

        String[] departureTimeParts = selectedTrain.getDepartureTime().split(":");
        int departureHour = Integer.parseInt(departureTimeParts[0]);
        int departureMinute = Integer.parseInt(departureTimeParts[1]);
        
        departureMinute += delayMinutes;
        if (departureMinute >= 60) {
            departureHour += departureMinute / 60;
            departureMinute = departureMinute % 60;
        }
        if (departureHour >= 24) {
            departureHour = departureHour % 24;
        }
        String newDepartureTime = String.format("%02d:%02d", departureHour, departureMinute);

        String[] arrivalTimeParts = selectedTrain.getArrivalTime().split(":");
        int arrivalHour = Integer.parseInt(arrivalTimeParts[0]);
        int arrivalMinute = Integer.parseInt(arrivalTimeParts[1]);

        arrivalMinute += delayMinutes;
        if (arrivalMinute >= 60) {
            arrivalHour += arrivalMinute / 60;
            arrivalMinute = arrivalMinute % 60;
        }
        if (arrivalHour >= 24) {
            arrivalHour = arrivalHour % 24;
        }
        String newArrivalTime = String.format("%02d:%02d", arrivalHour, arrivalMinute);

        selectedTrain.setDepartureTime(newDepartureTime);
        selectedTrain.setArrivalTime(newArrivalTime);

        System.out.println("Train delayed successfully.");
        System.out.println("Updated Train Details: " + selectedTrain);
    }
}
 class Train {
    private int trainId;
    private String name;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private double price;
    private String bookedBy;  // New attribute for storing the name of the person who booked the train
    private List<String> stations;  // New attribute for storing the intermediate stations

    // Constructor for creating a Train with intermediate stations
    public Train(int trainId, String name, String source, String destination, String departureTime, 
                 String arrivalTime, double price, List<String> stations) {
        this.trainId = trainId;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.stations = stations;
        this.bookedBy = null; // Initially no one has booked the train
    }

    public int getTrainId() {
        return trainId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public double getPrice() {
        return price;
    }

    public String getBookedBy() {
        return bookedBy;  // Getter for the bookedBy attribute
    }

    public List<String> getStations() {
        return stations;  // Getter for the stations attribute
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBookedBy(String bookedBy) {  // Setter for the bookedBy attribute
        this.bookedBy = bookedBy;
    }

    @Override
    public String toString() {
        return "Train [Train ID=" + trainId + ", Name=" + name + ", Source=" + source + ", Destination=" + destination +
                ", Departure=" + departureTime + ", Arrival=" + arrivalTime + ", Price=$" + price + 
                (bookedBy != null ? ", Booked by=" + bookedBy : "") + 
                ", Stations=" + String.join(" -> ", stations) + "]";
    }
}
