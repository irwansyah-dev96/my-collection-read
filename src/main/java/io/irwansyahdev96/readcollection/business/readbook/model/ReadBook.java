package io.irwansyahdev96.readcollection.business.readbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_read_book")
public class ReadBook {
    
    @Id
    @GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36,nullable = false)
    private String id;

    @Column(name = "page_of_read",length=5)
    private Integer pageOfRead;

    @Column(name = "date_of_read",nullable = false)
    private Long dateOfRead;

    @Column(name = "issbn",nullable = false)
    private String bookIssbn;

    @Column(name = "note")
    private String note;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPageOfRead() {
        return pageOfRead;
    }

    public void setPageOfRead(Integer pageOfRead) {
        this.pageOfRead = pageOfRead;
    }
    
    public Long getDateOfRead() {
        return dateOfRead;
    }

    public void setDateOfRead(Long dateOfRead) {
        this.dateOfRead = dateOfRead;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBookIssbn() {
        return bookIssbn;
    }

    public void setBookIssbn(String bookIssbn) {
        this.bookIssbn = bookIssbn;
    }

    
}
