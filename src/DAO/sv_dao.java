package DAO;

import java.util.ArrayList;

import ConnectMysql.Connectmysql;
import Model.SinhVien;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class sv_dao implements DAOInterface<SinhVien>{

	@Override
	public int add(SinhVien t) {
		int check=0;
		
		Connection connect=Connectmysql.getConnection();
		try {
			java.sql.Statement st=connect.createStatement();
			String sql="INSERT INTO sv(name,age,score) "+
			" VALUES ( '"+t.getName()+"' ,"+t.getAge()+", "+t.getDiem()+")";
			check=st.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return check;
	}
    public boolean check(String name) {
    	boolean check=false;
         ResultSet chek;
		
		Connection connect=Connectmysql.getConnection();
		try {
			java.sql.Statement st=connect.createStatement();
			String sql="Select name from sv where name='"+name+"'";
			chek=st.executeQuery(sql);
			if(chek.next()) {
				check=true;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return check;
    }
	@Override
	public int update(SinhVien t) {
		
		return 0;
	}

	@Override
	public int delete(SinhVien t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<SinhVien> selectAll() {
		ArrayList<SinhVien> sv_list=new ArrayList<SinhVien>();
		
		Connection connect=Connectmysql.getConnection();
		try {
			java.sql.Statement st=connect.createStatement();
			String sql="Select * from sv ";
			
			ResultSet rs=st.executeQuery(sql);
			while(rs.next()) {
				String name=rs.getString("name");
				int age=Integer.parseInt(rs.getString("age"));
				float score=Float.parseFloat(rs.getString("score"));
				SinhVien sv=new SinhVien(name,score,age);
				
				sv_list.add(sv);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return sv_list;
	}

	@Override
	public SinhVien selectById(SinhVien t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<SinhVien> selectByCondition(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

}
