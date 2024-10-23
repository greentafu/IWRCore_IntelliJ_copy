package mit.iwrcore.IWRCore.security.service;

import lombok.RequiredArgsConstructor;
import mit.iwrcore.IWRCore.entity.*;
import mit.iwrcore.IWRCore.repository.LineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    public List<String> getLines(){
        List<Line> lines=lineRepository.findAll();
        List<String> list=new ArrayList<>();
        lines.stream().forEach(x->list.add(x.getLineName()));
        return list;
    }

    public Line stringToLine(String str){
        Line line=Line.builder().lineName(str).build();
        return line;
    }
    public String lineToString(Line line){
        return line.getLineName();
    }
}
