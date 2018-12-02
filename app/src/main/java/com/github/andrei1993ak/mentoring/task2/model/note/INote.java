package com.github.andrei1993ak.mentoring.task2.model.note;

import java.io.Serializable;

public interface INote extends Serializable {

    Long getId();

    String getTitle();

    String getDescription();

    boolean isFavourite();
}
