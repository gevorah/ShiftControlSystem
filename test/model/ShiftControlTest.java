package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import customExceptions.AlreadyHasShiftException;

class ShiftControlTest {

	private ShiftControl sc = new ShiftControl();
	
	public void setup1() {
		sc.addUser(1,"123","Adolf","G.L.","","");
	}
	
	public void setup2() {
		sc.addUser(2,"369","Ric","M.L.","","");
		sc.addShift();
	}
	
	public void setup3() throws AlreadyHasShiftException {
		sc.addUser(1,"666","Gev","B.N.","","");
		sc.addUser(1,"999","Bin","G.N.","","");
		sc.addShift();
		sc.registerShift("666");
		sc.registerShift("999");
	}
	
	@Test
	void addUserTest() {
		try {
			sc.addUser(1,"321","Leon","M.C.","3693","agl");
			assertEquals(User.CC,sc.selectUser("321").getDocumentType(),"F 1");
			assertEquals("Leon",sc.selectUser("321").getNames(),"F 1");
			assertEquals("M.C.",sc.selectUser("321").getLastNames(),"F 2");
			assertEquals("3693",sc.selectUser("321").getPhone(),"F 3");
			assertEquals("agl",sc.selectUser("321").getAddress(),"F 4");
			sc.getUsers().clear();
			sc.addUser(3,"123","Marc","M.Z.","","");
			assertEquals(User.RC,sc.selectUser("123").getDocumentType(),"F 1");
		}catch(NullPointerException e) {
			System.err.println("Catch: addUser. "+e.getMessage());
		}	
	}

	@Test
	void selectUserTest() {
		try {
			setup1();
			assertEquals("Adolf",sc.selectUser("123").getNames(),"F 1");
			assertEquals("Raud",sc.selectUser("0").getNames(),"F 2");
			sc.getUsers().clear();
			assertEquals("Rold",sc.selectUser("0").getNames(),"F 3");
			}catch(NullPointerException e) {
			System.err.println("Catch: selectUser. "+e.getMessage());
		}
	}
	
	@Test
	void registerShiftTest(){
		try {
			setup2();
			sc.registerShift("369");
			sc.registerShift("369");
		}catch(AlreadyHasShiftException e) {
			System.err.println("Catch: registerShift. "+e.getMessage());
		}catch(NullPointerException e) {
			System.err.println("Catch: registerShift. "+e.getMessage());
		}
	}
	
	@Test
	void addShiftTest() {
		try {
			setup2();
			sc.addShift();
			assertEquals("A01",sc.getShifts().get(1).getCode(),"F 1");
			for(int i=0;i<98;i++) {
				sc.addShift();
			}
			assertEquals("A99",sc.getShifts().get(sc.getShifts().size()-1).getCode(),"F 2");
			sc.addShift();
			assertEquals("B00",sc.getShifts().get(sc.getShifts().size()-1).getCode(),"F 3");
			sc.getShifts().clear();
			sc.addShift();
			assertEquals("A00",sc.getShifts().get(sc.getShifts().size()-1).getCode(),"F 4");
		}catch(Exception e) {
			System.err.println("Catch: addShift. "+e.getMessage());
		}
	}
	
	@Test
	void selectToAttendShiftTest() {
		try {
			setup3();
			assertEquals("Not Attended",sc.selectToAttendShift().getStatus(),"F 1");
			assertEquals("666",sc.selectToAttendShift().getUser().getId(),"F 2");
			sc.attendShift(1);
			assertEquals("Not Attended",sc.selectToAttendShift().getStatus(),"F 1");
		}catch(NullPointerException e) {
			System.err.println("Catch: selectToAttendShift. "+e.getMessage());
		}catch (AlreadyHasShiftException e) {
			System.err.println("Catch: selectToAttendShift. "+e.getMessage());
		}
	}
	
	@Test
	void attendShiftTest() {
		try {
			setup3();
			sc.attendShift(1);
			assertEquals("Attended",sc.getShifts().get(0).getStatus(),"F 1");
			sc.attendShift(2);
			assertEquals("No user",sc.getShifts().get(1).getStatus(),"F 2");
			sc.attendShift(2);
		}catch(NullPointerException e) {
			System.err.println("Catch: attendShift. "+e.getMessage());
		} catch (AlreadyHasShiftException e) {
			System.err.println("Catch: attendShift. "+e.getMessage());
		}
	}
}
