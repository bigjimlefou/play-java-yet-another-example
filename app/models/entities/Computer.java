package models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Computer entity managed by Ebean
 */
@Entity 
public class Computer extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;

    @Temporal(TemporalType.DATE)
    @Formats.DateTime(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Europe/Brussels")
    public Date introduced;

    @Temporal(TemporalType.DATE)
    @Formats.DateTime(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone="Europe/Brussels")
    public Date discontinued;
    
    @ManyToOne
    public Company company;
    
}

