package com.klef.fsad.exam;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "movie_table")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id")
    private int id;
    
    @Column(name = "movie_name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    
    @Column(name = "movie_status", length = 20)
    private String status;  // e.g., "Released", "Upcoming", "Blockbuster", etc.
    
    @Column(name = "director", length = 100)
    private String director;
    
    @Column(name = "rating")
    private Double rating;  // 0-10 scale
    
    @Column(name = "duration_minutes")
    private Integer duration;
    
    @Column(name = "language", length = 50)
    private String language;
    
    // Default Constructor
    public Movie() {
    }
    
    // Parameterized Constructor
    public Movie(String name, Date releaseDate, String status, String director, 
                 Double rating, Integer duration, String language) {
        this.name = name;
        this.releaseDate = releaseDate;
        this.status = status;
        this.director = director;
        this.rating = rating;
        this.duration = duration;
        this.language = language;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseDate=" + releaseDate +
                ", status='" + status + '\'' +
                ", director='" + director + '\'' +
                ", rating=" + rating +
                ", duration=" + duration +
                ", language='" + language + '\'' +
                '}';
    }
}