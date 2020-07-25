package io.learning.core.domain;

import static io.learning.core.domain.DistributedTransactionStatus.NEW;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributedTransaction implements Serializable {

    private static final long serialVersionUID = -8594438501671636539L;

    private String id;

    @Builder.Default
    private DistributedTransactionStatus status = NEW;

    @Builder.Default
    private transient List<DistributedTransactionParticipant> participants = Collections.emptyList();

    public DistributedTransaction(String id, DistributedTransactionStatus status) {
        super();
        this.id = id;
        this.status = status;
    }
    
    public DistributedTransaction clone() {
        return DistributedTransaction.builder().id(this.id).status(this.status).participants(this.participants).build();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DistributedTransaction other = (DistributedTransaction) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

}
