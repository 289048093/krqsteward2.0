package Vo;

import com.ejushang.steward.customercenter.domain.Comment;
import com.ejushang.steward.customercenter.domain.CustomerTag;

import java.util.List;
import java.util.Set;

/**
 * User:moon
 * Date: 14-8-18
 * Time: 下午3:37
 */
public class CommentVo {

    private Comment comment;
    private Set<CustomerTag> customerTags;

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Set<CustomerTag> getCustomerTags() {
        return customerTags;
    }

    public void setCustomerTags(Set<CustomerTag> customerTags) {
        this.customerTags = customerTags;
    }
}
