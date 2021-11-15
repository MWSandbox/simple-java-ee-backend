package com.mdevoc.counter;

import com.mdevoc.persistence.BaseEntity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "counter")
@Schema(description = "A counter stores a numeric value that can be incremented.")
public class Counter extends BaseEntity {

    @NotNull
    @Schema(description = "The current count of the counter.")
    private long count;

    @Override
    public boolean equals(Object object) {
        if (!super.equals(object)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Counter other = (Counter) object;
        return this.count == other.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), count);
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
