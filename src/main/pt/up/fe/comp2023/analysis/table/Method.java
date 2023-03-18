package pt.up.fe.comp2023.analysis.table;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Method {

    private String name;
    //private List<Map.Entry<Symbol, String>> parameters = new ArrayList<>();
    private final Map<String, Symbol> parameters;
    private Type returnType;
    private final Map<String, Symbol> localVariables;

    public Method(String name) {
        this.name = name;
        /*for (Symbol parameter : parameters){
            this.parameters.add(Map.entry(parameter,parameter.getName()));
        }*/
        this.parameters = new HashMap<String, Symbol>();
        this.localVariables = new HashMap<String, Symbol>();
        //this.returnType = returnType;
    }

    public List<String> getParameterTypes() {
        List<String> params = new ArrayList<>();

        for (Map.Entry<String, Symbol> parameter : parameters.entrySet()) {
            params.add(parameter.getKey());
        }

        return params;
    }

    public List<Symbol> getParameters() {

        /*List<Symbol> parameters = new ArrayList<>();

        for(Map.Entry<Symbol, String> parameter : this.parameters){
            parameters.add(parameter.getKey());
        }
        return parameters;*/
        return new ArrayList<>(this.parameters.values());
    }

    public void setParameters(Symbol parameter) {
        this.parameters.put(parameter.getName(),parameter);
    }

    public Type getReturnType() {
        return this.returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public List<Symbol> getLocalVariables() {
        return new ArrayList<>(this.localVariables.values());
    }

    public void setLocalVariable(Symbol variable) {
        this.localVariables.put(variable.getName(), variable);
    }

    public String getName(){
        return this.name;
    }
}
