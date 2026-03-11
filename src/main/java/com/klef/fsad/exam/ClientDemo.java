package com.klef.fsad.exam;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ClientDemo {
    
    private static SessionFactory sessionFactory;
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        try {
            // Create SessionFactory
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            
            System.out.println("=== Hibernate HQL Movie Management System ===");
            
            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Insert Movie Records");
                System.out.println("2. View All Movies");
                System.out.println("3. Update Movie Name and Status by ID (HQL with positional parameters)");
                System.out.println("4. Search Movies by Status");
                System.out.println("5. Delete Movie by ID");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        insertMovies();
                        break;
                    case 2:
                        viewAllMovies();
                        break;
                    case 3:
                        updateMovieNameAndStatus();
                        break;
                    case 4:
                        searchMoviesByStatus();
                        break;
                    case 5:
                        deleteMovieById();
                        break;
                    case 6:
                        System.out.println("Exiting... Thank you!");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
            scanner.close();
        }
    }
    
    // I. Insert records using persistent object
    private static void insertMovies() {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            // Create multiple movie objects
            Movie movie1 = new Movie("Inception", new Date(), "Released", "Christopher Nolan", 
                                     8.8, 148, "English");
            
            Movie movie2 = new Movie("The Dark Knight", new Date(), "Blockbuster", "Christopher Nolan", 
                                     9.0, 152, "English");
            
            Movie movie3 = new Movie("3 Idiots", new Date(), "Blockbuster", "Rajkumar Hirani", 
                                     8.4, 170, "Hindi");
            
            Movie movie4 = new Movie("Parasite", new Date(), "Released", "Bong Joon-ho", 
                                     8.6, 132, "Korean");
            
            Movie movie5 = new Movie("Dangal", new Date(), "Hit", "Nitesh Tiwari", 
                                     8.4, 161, "Hindi");
            
            // Save the movies (persistent objects)
            session.save(movie1);
            session.save(movie2);
            session.save(movie3);
            session.save(movie4);
            session.save(movie5);
            
            transaction.commit();
            System.out.println("5 movie records inserted successfully!");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting movies: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    private static void viewAllMovies() {
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            
            // HQL to fetch all movies
            Query<Movie> query = session.createQuery("FROM Movie", Movie.class);
            List<Movie> movieList = query.getResultList();
            
            if (movieList.isEmpty()) {
                System.out.println("No movies found in the database.");
            } else {
                System.out.println("\n=== All Movies ===");
                for (Movie movie : movieList) {
                    System.out.println(movie);
                }
                System.out.println("Total movies: " + movieList.size());
            }
            
        } catch (Exception e) {
            System.err.println("Error viewing movies: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // II. Update Name and Status based on ID using HQL with positional parameters
    private static void updateMovieNameAndStatus() {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            System.out.print("Enter Movie ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            System.out.print("Enter new Movie Name: ");
            String newName = scanner.nextLine();
            
            System.out.print("Enter new Movie Status (Released/Blockbuster/Upcoming/Hit): ");
            String newStatus = scanner.nextLine();
            
            // HQL with positional parameters (using ?1, ?2 format)
            // Note: In Hibernate 5, positional parameters use ?1, ?2, etc.
            String hql = "UPDATE Movie SET name = ?1, status = ?2 WHERE id = ?3";
            
            Query query = session.createQuery(hql);
            query.setParameter(1, newName);
            query.setParameter(2, newStatus);
            query.setParameter(3, id);
            
            int result = query.executeUpdate();
            
            transaction.commit();
            
            if (result > 0) {
                System.out.println("Movie updated successfully! " + result + " record(s) affected.");
                
                // Display the updated movie
                Movie updatedMovie = session.get(Movie.class, id);
                System.out.println("Updated Movie: " + updatedMovie);
            } else {
                System.out.println("No movie found with ID: " + id);
            }
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating movie: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // Additional: Search movies by status using HQL
    private static void searchMoviesByStatus() {
        Session session = null;
        
        try {
            session = sessionFactory.openSession();
            
            System.out.print("Enter movie status to search (e.g., Released, Blockbuster, Hit): ");
            String status = scanner.nextLine();
            
            // HQL with positional parameter
            Query<Movie> query = session.createQuery(
                "FROM Movie WHERE status = ?1", Movie.class);
            query.setParameter(1, status);
            
            List<Movie> movieList = query.getResultList();
            
            if (movieList.isEmpty()) {
                System.out.println("No movies found with status: " + status);
            } else {
                System.out.println("\n=== Movies with Status: " + status + " ===");
                for (Movie movie : movieList) {
                    System.out.println(movie);
                }
                System.out.println("Total movies: " + movieList.size());
            }
            
        } catch (Exception e) {
            System.err.println("Error searching movies: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    // Additional: Delete movie by ID using HQL
    private static void deleteMovieById() {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            
            System.out.print("Enter Movie ID to delete: ");
            int id = scanner.nextInt();
            
            // First check if movie exists
            Movie movie = session.get(Movie.class, id);
            if (movie == null) {
                System.out.println("No movie found with ID: " + id);
                transaction.rollback();
                return;
            }
            
            System.out.println("Movie to delete: " + movie);
            System.out.print("Are you sure you want to delete this movie? (yes/no): ");
            scanner.nextLine(); // Consume newline
            String confirmation = scanner.nextLine();
            
            if (confirmation.equalsIgnoreCase("yes")) {
                // HQL delete with positional parameter
                String hql = "DELETE FROM Movie WHERE id = ?1";
                Query query = session.createQuery(hql);
                query.setParameter(1, id);
                
                int result = query.executeUpdate();
                transaction.commit();
                
                System.out.println("Movie deleted successfully! " + result + " record(s) affected.");
            } else {
                transaction.rollback();
                System.out.println("Delete operation cancelled.");
            }
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting movie: " + e.getMessage());
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}