import java.io.*;
import java.util.*;

class Students{
    private String Name;
    private float Marks;

    public Students(String Name,float Marks){
        this.Name = Name;
        this.Marks = Marks;
    }

    String getName(){
        return Name;
    }

    float getMarks(){
        return Marks;
    }
}

class GradeManager{

    static int NumberofStudents = 0;
    static float maxMarks = 0f,minMarks = 0f,avgMarks = 0f,TotalMarks = 0f;
    static String MaxMarkStudent = "NA",MinMarkStudent = "NA";
    ArrayList<String> StudentList = new ArrayList<String>();
    ArrayList<Float> MarksList = new ArrayList<Float>();

    static void ClassReport(){

        System.out.println("\n============\nClass Report\n============\n");

        System.out.println("Number of Students : "+NumberofStudents);
        System.out.printf("Maximum Marks : %.2f (%s)\n",maxMarks,MaxMarkStudent);
        System.out.printf("Minimum Marks : %.2f (%s)\n",minMarks,MinMarkStudent);
        System.out.printf("Average Marks : %.2f\n",avgMarks);

        System.out.println("________________________\n");
    }

    void newstudent(Students s){
        CsvFileHandling.readClassRecords();

        StudentList.add(s.getName());
        MarksList.add(s.getMarks());

        CsvFileHandling.AddStudent(s);
        
        if(NumberofStudents == 0){ 
            maxMarks = s.getMarks(); 
            minMarks = s.getMarks(); 
            MaxMarkStudent = 
            MinMarkStudent = s.getName(); 
        }
        else{
            if(maxMarks<=s.getMarks()){ 
                maxMarks = s.getMarks(); 
                MaxMarkStudent = s.getName(); 
            }
            if(minMarks>=s.getMarks()){ 
                minMarks = s.getMarks(); 
                MinMarkStudent = s.getName(); 
            }
        }
        NumberofStudents++;
        TotalMarks += s.getMarks();
        avgMarks = (float)(TotalMarks/NumberofStudents);

        CsvFileHandling.writeclassRecords();
    }

    void displayNewlyAddedStudents(){

            if(StudentList.isEmpty()){
                System.out.println("\nNo recently Student Added");
                return;
            }

            ClassReport();
            Iterator<String> itr1 = StudentList.iterator();
            Iterator<Float> itr2 = MarksList.iterator();

            System.out.println("Newly Added Students\nName \t:\tMarks");
            System.out.println("_____________________________\n");
            
            while(itr1.hasNext() && itr2.hasNext()){
                System.out.println(itr1.next()+" : "+itr2.next());
            }
            System.out.println("_____________________________\n");
    }
}

class CsvFileHandling{

    static String studentFile = System.getProperty("user.dir") + "\\StudentMarkList.csv";
    static String reportFile = System.getProperty("user.dir") + "\\GradeManager.csv";

    static void AddStudent(Students s){
        try{
            FileWriter fwrite = new FileWriter(studentFile,true);
            fwrite.write(s.getName()+","+s.getMarks()+"\n");
            fwrite.flush();
            fwrite.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }

    void displayClassList(){
        try{
            readClassRecords();

            if(GradeManager.NumberofStudents == 0){
                System.out.println("\n[Warning] No Student data available");
                return;
            }

            BufferedReader bfread = new BufferedReader(new FileReader(studentFile));
            String Line;
            int i = 1;
            System.out.println("\n===============Students=============\n\nS.No\tName of the Students : \tMarks");
            System.out.println("_______________________________________\n");
            
            while((Line = bfread.readLine())!= null){
                String[] data = Line.split(",");
                System.out.println((i++)+".\t"+data[0]+" : "+data[1]);
            }
            bfread.close();
            GradeManager.ClassReport();
        }
        catch(Exception e){ 
            System.out.println(e);
        }
    }

    static void readClassRecords(){
        try{
            BufferedReader bfread = new BufferedReader(new FileReader(reportFile));
            String[] data = bfread.readLine().split(",");
            GradeManager.NumberofStudents = Integer.parseInt(data[0]);
            GradeManager.maxMarks = Float.parseFloat(data[1]);
            GradeManager.minMarks = Float.parseFloat(data[2]);
            GradeManager.avgMarks = Float.parseFloat(data[3]);
            GradeManager.TotalMarks = Float.parseFloat(data[4]);
            String[] data1 = bfread.readLine().split(",");
            GradeManager.MaxMarkStudent = data1[0];
            GradeManager.MinMarkStudent = data1[1];
            bfread.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    static void writeclassRecords(){
        try{
            FileWriter fwrite = new FileWriter(reportFile);
            fwrite.write(GradeManager.NumberofStudents+","+GradeManager.maxMarks+","+GradeManager.minMarks+","+GradeManager.avgMarks+","+GradeManager.TotalMarks+"\n");
            fwrite.write(GradeManager.MaxMarkStudent+","+GradeManager.MinMarkStudent+"\n");
            fwrite.close();
        } catch(Exception e){ 
            System.out.println(e);
        }
    }
}

class RunOnce{
    static void runOnce(){
        File f1 = new File(CsvFileHandling.reportFile);
        File f2 = new File(CsvFileHandling.studentFile);
        if(f1.exists() && f2.exists()){
            return;
        }
        else{
            try{
                f1.createNewFile();
                FileWriter fw = new FileWriter(f1);
                fw.write("0,0,0,0,0\nNA,NA\n");
                fw.close();
                f2.createNewFile();
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }
}

public class StudentGradeTrackerSystem{
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        GradeManager g = new GradeManager();
        int choice;

        RunOnce.runOnce();

        while(true){
            
            System.out.println("\n1. Add new Student");
            System.out.println("2. Display Newly Added Students");
            System.out.println("3. Generate Class Report");
            System.out.println("4. Exit");

            System.out.print("Enter choice : ");
            if(!input.hasNextInt()){
                System.out.println("Enter valid Choice!");
                input.nextLine();
                continue;
            }
            choice = input.nextInt();
            input.nextLine();

            switch(choice){
                case 1:
                    String Name;
                    int Marks;
                    while (true) {
                        try{
                            System.out.print("Enter Name : ");
                            Name = input.nextLine();
                            if(!Name.matches("[a-zA-Z\\s]+")) {
                                System.out.println("Enter Vaild Name! It should be contain Only Alphabets!");
                            }else{
                                break;
                            }
                        }catch(Exception e){
                            System.out.println("Enter valid Name");
                        }
                    }
                    while (true) {
                        try{
                            System.out.print("Enter Marks : ");
                            Marks = input.nextInt();
                            if ( Marks > 100 || Marks <= 0 ){
                                System.out.println("Enter Marks only betweeen (1 to 100)");
                            }
                            else{
                                break;
                            }
                        }catch(InputMismatchException e){
                            System.out.println("Enter valid marks! between(1 to 100)");
                            input.nextLine();
                        }
                    }
                    Students student = new Students(Name.trim(), Marks);
                    g.newstudent(student);
                    break;
                case 2:
                    g.displayNewlyAddedStudents();
                    break;
                case 3:
                    CsvFileHandling obj = new CsvFileHandling();
                    obj.displayClassList();
                    break;
                case 4:
                    input.close();
                    return;
                default:
                    System.out.println("\nInvalid choice");
                    break;
            }
        }
    }
}