package com.example.studynotebackend.service;

import com.example.studynotebackend.domain.Note;
import com.example.studynotebackend.mapper.NoteMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(int page, int size, String keyword) {
        int offset = page * size;
        return noteMapper.findPage(offset, size, keyword);
    }

    public Note getById(Long id) {
        Note note = noteMapper.findById(id);
        if (note == null) {
            throw new RuntimeException("笔记不存在");
        }
        return note;
    }

    @Transactional
    public Note create(Note note) {
        noteMapper.insert(note);
        return note;
    }

    @Transactional
    public Note update(Long id, String title, String content) {
        Note note = getById(id);
        note.setTitle(title);
        note.setContent(content);
        noteMapper.update(note);
        return note;
    }

    @Transactional
    public void delete(Long id) {
        getById(id);
        noteMapper.delete(id);
    }
}
