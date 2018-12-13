package com.example.employ;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import com.google.gson.*;

@Path("/")
public class EmployManage {

    private static List<Employee> employeeList = new ArrayList<Employee>();

/*    @GET
    @Path("employees")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMessage(){
        JSONObject obj = new JSONObject();
        obj.put("something", 15);
        return obj.toString();
    }*/

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response forFun(){
        return Response.ok("Hi!!!", MediaType.TEXT_PLAIN).build();
    }

    @GET
    @Path("/ducnk/employees")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEmployees()
    {
        JSONObject obj = new JSONObject();
        Gson objg = new Gson();

        return objg.toJson(employeeList);
    }

    @GET
    @Path("/ducnk/employees/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getEmployeeById(@PathParam("id") Integer id){
        for (Employee employee : employeeList){
            if (employee.getId() == id){
                return Response.ok(employee.toString(), MediaType.APPLICATION_JSON).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Not found employee with id " + id).build();
    }

    @GET
    @Path("/ducnk/employees/search")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response searchEmployee(
        @DefaultValue("") @QueryParam("created_by") String created_by,
        @DefaultValue("") @QueryParam("content") String content,
        @DefaultValue("0") @QueryParam("from_date") long from_date){

        List<Employee> resultEmployee = new ArrayList<Employee>();
        resultEmployee.clear();

        for (Employee employee : employeeList){
            if (employee.getContent().toLowerCase().contains(content.toLowerCase())
                    || employee.getCreated_by().equalsIgnoreCase(created_by) || employee.getCreatedDate() >= from_date){
                resultEmployee.add(employee);
            }
        }

        if (resultEmployee.isEmpty()){
            return Response.status(400).entity("Not found employee !!").build();
        }
        else{
            return Response.ok(new Gson().toJson(resultEmployee), MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/ducnk/employees")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEmployee( String receivedEmployee ) throws URISyntaxException
    {
        Gson gson = new Gson();
        Employee employee = gson.fromJson(receivedEmployee, Employee.class);
        if(employee == null){
            return Response.status(400).entity("Please add employee details !!").build();
        }

        if (employee.getId() <= 0){
            return Response.status(400).entity("Please provide the employee ID !!").build();
        }

        if(employee.getContent() == null || employee.getContent().trim().length() == 0) {
            return Response.status(400).entity("Please provide the content !!").build();
        }

        for (Employee existEmployee : employeeList){
            if (existEmployee.getId() == employee.getId()){
                return Response.status(400).entity("ID employee exist !!").build();
            }
        }

        employeeList.add(new Employee(  employee.getId(),
                                        employee.getContent(),
                                        employee.getCreated_by()));
        return Response.ok(employee.toString(), MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/ducnk/employees/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEmployeeById(@PathParam("id") Integer id, String receivedEmployee)
    {
        Gson gson = new Gson();
        Employee employee = gson.fromJson(receivedEmployee, Employee.class);
        Employee updatedEmployee = new Employee();

        if (employee.getId() <= 0){
            return Response.status(400).entity("Please provide the employee ID !!").build();
        }

        if(employee.getContent() == null || employee.getContent().trim().length() == 0) {
            return Response.status(400).entity("Please provide the content !!").build();
        }

        boolean existId = false;
        for (Employee existEmployee : employeeList){
            if (existEmployee.getId() == employee.getId()){
                updatedEmployee = existEmployee;
                employeeList.remove(existEmployee);
                existId = true;
                break;
            }
        }

        if (!existId){
            return Response.status(400).entity("Employee ID does not exist !!").build();
        }

        updatedEmployee.update( employee.getId(),
                                employee.getContent(),
                                employee.getUpdated_by());


        employeeList.add(updatedEmployee);
        Gson objg = new Gson();

        return Response.ok(objg.toJson(updatedEmployee), MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("/ducnk/employees/{id}")
    public Response deleteEmployeeById(@PathParam("id") Integer id)
    {
        for (Employee employee : employeeList){
            if (employee.getId() == id){
                employeeList.remove(employee);
                return Response.status(202).entity("Employee deleted successfully !!").build();
            }
        }

        return Response.status(400).entity("Employee ID does not exist !!").build();
    }
}
