package himalia.model;

import java.util.ArrayList;

import junit.framework.TestCase;

public class TestProtectedRegion extends TestCase {
    ProtectedRegion r;

    Word w1,w13;
    
    Word w2,w8;

    Word w3,w9;
    Word w4,w10;
    Word w5,w11;
    Word w6,w12;
    Word w14,w15,w16;
    Poem p1,p2;

    boolean p3;
    
   
    protected void setUp(){
        int width=10;
        int height=20;
        Position pos=new Position(0,0,0);
        
        w1=new Word(10,10, 0,"Spring", WordType.noun);
        
        w2=new Word(11,20, 0,"aaa", WordType.adj);//
    
        w3=new Word(12,30, 0,"aba", WordType.adv);//
        w4=new Word(13,20, 0,"abb", WordType.adj);//
        w5=new Word(14,20, 0,"abc", WordType.adj);//
        w6=new Word(15,20, 0,"acc", WordType.adj);//
        
        w8=new Word(50,10, 0,"Spring", WordType.noun);//
        
        w9=new Word(51,20, 0,"aaa", WordType.adj);//
    
        w10=new Word(52,30, 0,"aba", WordType.adv);
        w11=new Word(53,20, 0,"abb", WordType.adj);//
        w12=new Word(54,20, 0,"abc", WordType.adj);//
        w13=new Word(55,20, 0,"acc", WordType.adj);//
        w14=new Word(14,50, 0,"abc", WordType.adj);//
        w15=new Word(30,60, 0,"acc", WordType.adj);//
        w16=new Word(70,90, 0,"acc", WordType.adj);//
        pos=new Position(0,0,0);
        ArrayList<Word> ws=new ArrayList<Word>();
        ws.add(w1);
        ws.add(w2);
        ws.add(w3);
        ws.add(w4);
        ws.add(w5);
        ws.add(w6);
        ws.add(w8);
        ws.add(w9);
        ws.add(w10);
        ws.add(w11);
        ws.add(w12);
        ws.add(w13);
        ws.add(w14);
        ws.add(w15);
        ws.add(w16);
        r=new ProtectedRegion(width, height, pos);

        r.words=ws;
        p1=r.convertWordToPoem(w12);//deleted later
        p2=r.convertWordToPoem(w13);//don't delete
        
        
    }
    
    public void testaddPoem(){
        //Word w7=new Word(40,60, 0,"ccc", WordType.adj);
        Poem p3=r.convertWordToPoem(w6);
        int j=r.getPoems().size();
        r.addPoem(p3);;
        assertEquals(r.getPoems().size(), j+1);
    }
    
    public void testaddWord(){//*w7
        Word w7=new Word(40,60, 0,"ccc", WordType.adj);
        int j=r.getWords().size();
        assertEquals(r.addWord(w7), true);
        assertEquals(r.getWords().size(), j+1);
        for(Word w:r.getWords()){
            if(w.getID()==w7.getID()){
                assertEquals(w.getValue(), "ccc");
            }
        }
        
    }
    
    public void testdeleteWord(){//*w2
        //delete
        Word w7=new Word(80,30, 0,"kkk", WordType.adj);
        int j=r.getWords().size();
        assertEquals(r.deleteWord(w2), w2);
        assertEquals(r.getWords().size(), j-1);

        j=r.getWords().size();
        assertEquals(r.deleteWord(w7), null);
        assertEquals(r.getWords().size(), j);
        }

    public void testdeletepoem(){
        int i=r.poems.size();
        
        assertEquals(r.poems.size(), i);
        assertEquals(r.deletePoem(p1), p1);
        assertEquals(r.poems.size(), i-1);
        
        Poem kk=new Poem();
        assertEquals(r.deletePoem(kk), null);
        
    }

/*
    
    public void testmoveObject(){//w3
        
        Position des=new Position(20,1,0);
        Move mm1=new Move(w3.getID(),ObjectType.WordType,w3.getPosition(),des);
        Move mmm1=r.moveObject(ObjectType.WordType, w3.getID(), des);
        assertEquals(mmm1.destinationPosition,mm1.destinationPosition);
        assertEquals(mmm1.sourcePosition,mm1.sourcePosition);
        assertEquals(w3.getPosition(), des);
        
        
        Position des1=new Position(55,49,0);
        Move mm2=new Move(p2.getID(),ObjectType.PoemType,p2.getPosition(),des1);
        Move mmm2=r.moveObject(ObjectType.PoemType, p2.getID(), des1);
        assertEquals(mm2.destinationPosition,mmm2.destinationPosition);
        assertEquals(mm2.sourcePosition,mmm2.sourcePosition);
        assertEquals(mm2.getObjectID(),mmm2.getObjectID());
       //assertEquals(p2.getPosition(), des1);

        
        Move mmmmm=r.moveObject(ObjectType.RowType, w3.getID(), des);
        assertEquals(mmmmm,null);
    }
    
    */
    
    public void testconnectWords(){
//        int i=r.poems.size();
//        int j=r.words.size();
//        
//        Word w7=new Word(80,30, 0,"kkk", WordType.adj);
//        //assertEquals(r.connectWord(w7, w1), false);
//        //assertEquals(r.poems.size(), i);
//        //assertEquals(r.words.size(), j);
//        
//        assertEquals(r.connectWord(null, null), false);
//        assertEquals(r.poems.size(), i);
//        assertEquals(r.words.size(), j);
//
//        assertEquals(r.connectWord(w11, w4), true);
//        assertEquals(r.poems.size(), i+1);
//        assertEquals(r.words.size(), j-2);
//      

    }
    


    public void testconnectWordToPoem(){
//        int i=r.poems.size();
//        int j=r.words.size();
//        Word w7=new Word(80,30, 0,"kkk", WordType.adj);
//        //assertEquals(r.connectWordToPoem(w7, p2), false);
//        
//        assertEquals(r.connectWordToPoem(w5, p2), true);
//        assertEquals(r.poems.size(), i);
//        assertEquals(r.words.size(), j-1);
        
    }
    //error in this test
    public void testdisconnectEdgeWord(){
        int i=r.poems.size();
        int j=r.words.size();
        System.out.println("Hi");
        assertEquals(r.connectWordToPoem(w10, p2), true);
       
        assertEquals(r.poems.size(), i);
        assertEquals(r.words.size(), j-1);
        assertEquals(w10.intersect(w10.getPosition().x, w10.getPosition().y), true);
        
        assertEquals(r.disconnectEdgeWord(p2.getID(), w10.getPosition()), true);//error two
        /*
        assertEquals(r.poems.size(), i-1);
        assertEquals(r.words.size(), j+2);
        */
        //assertEquals(r.disconnectEdgeWord(1000, w10.getPosition()), false);

    }
    /*
    public void testdisconnectRow(){
        int i=r.poems.size();
        int j=r.words.size();
        Position des=new Position(20,1,0);
        assertEquals(r.disconnectRow(10000, des), false);

    }
    */
    
    public void testconvertwordtopoem(){
        int i=r.getPoems().size();
        int j=r.getWords().size();

        Poem po=r.convertWordToPoem(w6);
        assertEquals(po, r.getPoem(po.getID()));
        assertEquals(po.getRows().get(0).getWords().get(0).getValue(), w6.getValue());
        assertEquals(r.getPoems().size(), i+1);
        assertEquals(r.getWords().size(), j-1);
        Word w7=new Word(80,30, 0,"kkk", WordType.adj);
        //Poem p0=r.convertWordToPoem(w7);
        //assertEquals(r.getPoems().size(), i+1);
        //assertEquals(r.getWords().size(), j-1);
        //assertEquals(p0, null);

    }
    
    public void testconnectPoemToPoem(){
        int i=r.getPoems().size();
        int j=r.getWords().size();
        Poem p0=r.convertWordToPoem(w14);
        Poem p1=r.convertWordToPoem(w15);
        assertEquals(r.connectPoemToPoem(p0, p1),true);
        assertEquals(r.getPoems().size(), i+1);
        assertEquals(r.getWords().size(), j-2);
        //assertEquals(r.connectPoemToPoem(100, 50),false);//edit later
    }
  
    public void testgetWordID(){
        Word w7=new Word(80,30, 0,"kkk", WordType.adj);
        int id=r.getWordID(w8.getPosition());
       // assertEquals(id, w8.getID());
        int id1=r.getWordID(w7.getPosition());
        assertEquals(id1,-1);
    }


    public void testgetPoemID(){
        Position posw=new Position(1000,1000,1);


      // assertEquals(r.getPoemID(p2.getPosition()), p2.getID());

        assertEquals(r.getPoemID(posw), -1);
    }


   
    public void testgetPoem(){

        Poem ppp=r.getPoem(p2.getID());
        assertEquals(ppp, p2);
        assertEquals(r.getPoem(9999), null);
    }
   
    public void testgetWord(){
        Word w7=new Word(80,30, 0,"kkk", WordType.adj);
        Word tt=r.getWord(w9.getID());
        assertEquals(tt.getID(), w9.getID());
        Word ttt=r.getWord(w7.getID());
        assertEquals(ttt, null);
        
    }

    public void testgetPoems(){
        int i=r.poems.size();
        assertEquals(i, r.getPoems().size());
    }
    public void testgetWords(){
        int i=r.words.size();
        assertEquals(i, r.getWords().size());
    }
    /**
     * Susan add
     * test:public boolean detectOverlap(Position pos)
     */
    public void testdetectOverlap(){
    	//w13=new Word(55,20, 0,"acc", WordType.adj);// width=3*3(55~64,10~20)
    	//case 1: overlap poem
    	Position p1=new Position(55,20,0);
    	boolean res=r.detectOverlap(p1);
    	assertEquals(true,res);
    	//case 2: overlap word
    	Position p2=new Position(10,10,0);
    	res=r.detectOverlap(p2);
    	assertEquals(true,res);
    	//case 3: safe move, no overlap
    	Position p3=new Position(150,150,0);
    	res=r.detectOverlap(p3);
    	assertEquals(false,res);
    }
    
    public void testgetPosition(){
        assertEquals(r.getPosition(),r.position);
    }
    
    public void testgetWidth(){
        assertEquals(r.getWidth(),r.width);
    }
    
    public void testgetHeight(){
        assertEquals(r.height,r.getHeight());
    }
    
    public void testremoveWord(){
        int i=r.words.size();
        r.removeWord(w9);
        assertEquals(i-1,r.words.size());
    }
    
    public void testwithinProtectedArea(){
        assertEquals(r.withinProtectedArea(3, 4),true);
        assertEquals(r.withinProtectedArea(-3, 4),false);
    }
    
    public void testisOccupiedbyObject(){
        r.isOccupiedbyObject(null);
    }
    
    public void testfindWord(){
        Word w99=new Word(400,600, 0,"ccc", WordType.adj);
        assertEquals(r.findWord(w99.getPosition()),null);
        
        assertEquals(r.findWord(w16.getPosition()),w16);
    }
    public void testgetIntersectedWords(){
        ArrayList<Word> wl=r.getIntersectedWords(w9);
        assertEquals(wl.size(),4);//number need to be checked
    }
    
    public void testgetIntersectedPoems(){
        ArrayList<Poem> pl=r.getIntersectedPoems(w9);
        assertEquals(pl.size(),2);//number need to be checked
    }
    
    public void testconnectEdgeWord(){
        assertTrue(r.connectEdgeWord(p2, w9));
    }

   

}
