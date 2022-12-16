package by.vedom.library.business.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class TaskSearchValues {
    private String title;
    private Integer completed;
    private Long priorityId;
    private Long categoryId;
    private String email;
    private Date dateFrom;
    private Date dateTo;

    // paging
    private Integer pageNumber;
    private Integer pageSize;

    // sorting
    private String sortColumn;
    private String sortDirection;
}
