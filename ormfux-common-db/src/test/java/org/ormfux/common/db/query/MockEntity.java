package org.ormfux.common.db.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ormfux.common.db.annotation.CollectionOfEntities;
import org.ormfux.common.db.annotation.Column;
import org.ormfux.common.db.annotation.Entity;
import org.ormfux.common.db.annotation.Id;
import org.ormfux.common.db.annotation.Version;
import org.ormfux.common.db.generators.RandomIdGenerator;

@Entity(table = "mock")
public class MockEntity {
    
    @Column(columnName = "id", columnLabel = "id")
    @Id(RandomIdGenerator.class)
    private String id;
    
    @Column(columnName = "creationDate", columnLabel = "creationDate")
    private Date creationDate;
    
    @Column(columnName = "version", columnLabel = "version")
    @Version
    private long version;
    
    @Column(columnName = "gross", columnLabel = "gross")
    private BigDecimal gross;
    
    @CollectionOfEntities(joinColumn = "mock")
    private List<MockEntity2> mockList1 = new ArrayList<>();
    
    @CollectionOfEntities(joinTable = "mock1_mock2", joinColumn = "mock", inverseJoinColumn = "mock2")
    private List<MockEntity2> mockList2 = new ArrayList<>();
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getId() {
        return id;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setVersion(long version) {
        this.version = version;
    }
    
    public long getVersion() {
        return version;
    }
    
    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }
    
    public BigDecimal getGross() {
        return gross;
    }
    
    public List<MockEntity2> getMockList1() {
        return mockList1;
    }
    
    public void setMockList1(final List<MockEntity2> mockList1) {
        this.mockList1 = mockList1;
    }
    
    public List<MockEntity2> getMockList2() {
        return mockList2;
    }
    
    public void setMockList2(final List<MockEntity2> mockList2) {
        this.mockList2 = mockList2;
    }
}