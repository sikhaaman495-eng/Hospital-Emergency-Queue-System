
import java.util.*;
import java.io.*;

class Patient {
    String id;
    String name;
    int severity;
    long arrivalTime;

    Patient(String id, String name, int severity, long arrivalTime) {
        this.id = id;
        this.name = name;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }

    public String toString() {
        return id + " | " + name + " | Severity " + severity;
    }
}

public class HospitalEmergencyQueue {

    static LinkedList<Patient> patients = new LinkedList<>();

    static PriorityQueue<Patient> queue = new PriorityQueue<>(
        (a, b) -> {
            if (a.severity != b.severity) {
                return b.severity - a.severity;
            }
            return Long.compare(a.arrivalTime, b.arrivalTime);
        }
    );

    static int patientNumber = 101;
    static long arrivalCounter = 0;

    static final String FILE = "emergency_queue.txt";

   
    static void registerPatient(String name, int severity) {

        if (severity < 1 || severity > 5) {
            System.out.println("Invalid severity! Enter 1 to 5.");
            return;
        }

        String id = "P" + patientNumber++;

        Patient p = new Patient(id, name, severity, ++arrivalCounter);

        patients.add(p);
        queue.add(p);

        System.out.println("Patient added: " + p);
    }

    
    static void callNextPatient() {

        if (queue.isEmpty()) {
            System.out.println("No patients in the queue.");
            return;
        }

        Patient p = queue.poll();
        patients.remove(p);

        System.out.println("Calling " + p);
    }

    
    static void searchPatient(String id) {

        for (Patient p : patients) {

            if (p.id.equalsIgnoreCase(id)) {
                System.out.println("Patient found: " + p);
                return;
            }
        }

        System.out.println("Patient " + id + " not found.");
    }

    
    static void displayQueue() {

        if (queue.isEmpty()) {
            System.out.println("No patients in the queue.");
            return;
        }

        PriorityQueue<Patient> temp = new PriorityQueue<>(queue);

        System.out.println("Waiting Patients:");

        while (!temp.isEmpty()) {
            System.out.println(temp.poll());
        }
    }

    
    static void saveData() {

        try {
            FileWriter writer = new FileWriter(FILE);

            for (Patient p : patients) {
                writer.write(p.id + "|" + p.name + "|" +
                    p.severity + "|" + p.arrivalTime + "\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

   
    static void loadData() {

        File file = new File(FILE);

        if (!file.exists()) {
            return;
        }

        try {
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                String[] data = line.split("\\|");

                if (data.length == 4) {

                    String id = data[0];
                    String name = data[1];
                    int severity = Integer.parseInt(data[2]);
                    long time = Long.parseLong(data[3]);

                    if (severity >= 1 && severity <= 5) {

                        Patient p = new Patient(id, name, severity, time);

                        patients.add(p);
                        queue.add(p);

                        arrivalCounter = Math.max(arrivalCounter, time);

                        int number = Integer.parseInt(id.substring(1));
                        patientNumber = Math.max(patientNumber, number + 1);
                    }
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }

   
    public static void main(String[] args) {

        loadData();

        Scanner sc = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n===== HOSPITAL EMERGENCY QUEUE =====");
            System.out.println("1. Register Patient");
            System.out.println("2. Call Next Patient");
            System.out.println("3. Search Patient");
            System.out.println("4. Display Waiting Queue");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter patient name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter severity (1-5): ");
                    int severity = sc.nextInt();
                    sc.nextLine();

                    registerPatient(name, severity);
                    break;

                case 2:
                    callNextPatient();
                    break;

                case 3:
                    System.out.print("Enter patient ID: ");
                    String id = sc.nextLine();

                    searchPatient(id);
                    break;

                case 4:
                    displayQueue();
                    break;

                case 5:
                    saveData();
                    System.out.println("Data saved. Exiting system.");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }
}
