package himalia.view;

import himalia.controller.RefreshPanelController;
import himalia.model.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * This class is used to creating panel object for drawing
 * @author Yan Wang
 *
 */
public class RegionPanel extends JPanel {
	
	Model model;
	Image offscreenImage;
	Graphics offscreenGraphics;
	Graphics canvasGraphics;
	int height = 600;
	int width = 500;

	// Current mouse listener
	MouseListener        activeListener;
	MouseMotionListener  activeMotionListener;
	
	/** Properly register new listener (and unregister old one if present). */
	public void setActiveListener(MouseListener ml) {
		this.removeMouseListener(activeListener);
		activeListener = ml;
		if (ml != null) { 
			this.addMouseListener(ml);
		}
	}
	
	/** Properly register new motion listener (and unregister old one if present). */
	public void setActiveMotionListener(MouseMotionListener mml) {
		this.removeMouseMotionListener(activeMotionListener);
		activeMotionListener = mml;
		if (mml != null) {
			this.addMouseMotionListener(mml);
		}
	}

	public RegionPanel(Model model){
		super();
		this.model = model;
		model.getBoard().addListener(new RefreshPanelController(this));
		
		initialize();
	}


	void initialize() {		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		ensureImageAvailable(g);
		g.drawImage(offscreenImage, 0, 0, getWidth(), getHeight(), this);
		g.drawLine(0,this.getHeight()/2,this.getWidth(),this.getHeight()/2);
		redraw();



	}


	public void redraw() {
		// nothing to draw into? Must stop here.
		if (offscreenImage == null) return;
		
		offscreenGraphics.drawLine(0,this.getHeight()/2,this.getWidth(),this.getHeight()/2);
		// clear the image.
		offscreenGraphics.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		/** Draw all objects. */
		ArrayList<Word> wrds =  model.getBoard().getUnProtectedRegion().getWords();
		for (Word w :wrds) {
			paintShape(offscreenGraphics, w);
		}
		
		wrds = model.getBoard().getProtectedRegion().getWords();
		
		for (Word w : wrds) {
			paintShape(offscreenGraphics, w);
		}
		
		ArrayList<Poem> poems = model.getBoard().getProtectedRegion().getPoems();

		for (Poem p : poems) {
			for(Row r : new ArrayList<Row>(p.getRows().values())){
				for(Word w : r.getWords()){
					paintShape(offscreenGraphics, w, 1);
				}
			}
		}
		
		if (model.getBoard().getHighlightedWord()!=null){
			paintShape(offscreenGraphics, model.getBoard().getHighlightedWord(), 2);
		}
		
	}


	/**
	 * Draw each object according to colorFlag
	 * @param g
	 * @param w
	 * @param colorFlag
	 */
	public void paintShape(Graphics g, Word w, int... colorFlag) {

		if (g == null) { return; }
	    assert colorFlag.length <= 1;
	    int color = colorFlag.length > 0 ? colorFlag[0] : 0;
		
	    if(color==0){
	    	g.setColor(Color.pink);
	    }
	    else if(color==1)
	    {
	    	g.setColor(Color.green);
	    }	
	    else{
	    	g.setColor(Color.yellow);
	    }
		g.fillRect(w.getPosition().x, w.getPosition().y, w.getWidth(), w.getHeight());
		g.setColor(Color.black);
		g.drawString(w.getValue(), w.getPosition().x+1/2*w.getWidth(), w.getPosition().y + w.getHeight()*3/4);
		
	}

	/** Make sure that image is created as needed. */
	void ensureImageAvailable(Graphics g) {
		if (offscreenImage == null) {
			offscreenImage = this.createImage(this.getWidth(), this.getHeight());
			offscreenGraphics = offscreenImage.getGraphics();
			canvasGraphics = g;
			
			redraw();
		}
	}
	
	public void paintShape(Word w) {
		paintShape(canvasGraphics, w);
	}
	
	/** Repaint to the screen just the given part of the image. */
	public void paintBackground(Word w) {
		// Only updates to the screen the given region
		if (canvasGraphics != null) {
			canvasGraphics.drawImage(offscreenImage, w.getPosition().x, w.getPosition().y, w.getWidth(), w.getHeight(), this);
			repaint(w.getPosition().x, w.getPosition().y, w.getWidth(), w.getHeight());
		}
	}
	


	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height); 
	}

	/**
	 * This method paints the poem on the board.
	 * @param selected
	 */
	public void paintBackground(Poem selected) {
		
	}

	

}
