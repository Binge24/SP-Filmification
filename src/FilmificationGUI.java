
import de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Binge2/Ryuusei
 */

public class FilmificationGUI extends javax.swing.JFrame 
{
    private ArrayList<JLabel> screenshots = new ArrayList<JLabel>();
    private int numOfImages;
    private int currentIndex;
    private Timer timer;
    boolean paused = false;
    private String filmSelected = "MultistageNetwork";
    
    //NOTE
    private MultistageNetwork mn = new MultistageNetwork();
    //add imoha dri benj
    
    public FilmificationGUI(int numOfImages, boolean isTileViewDefault) 
            throws UnsupportedLookAndFeelException, ParseException, URISyntaxException 
    {
        UIManager.setLookAndFeel(new SyntheticaBlackMoonLookAndFeel());
        
        initComponents();
        
        setNumOfImages(numOfImages);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        if(isTileViewDefault)
        {
            setDefaultView(tileViewButton);
        }
        else
        {
            setDefaultView(fullViewButton);
        }
        
        setCurrentIndex(0);
        
        playOrPauseButton.addItemListener(new ItemListener() 
        {
            public void itemStateChanged(ItemEvent ev) 
            {
                if(ev.getStateChange()==ItemEvent.SELECTED)
                {
                    System.out.println("button is selected");
                    paused = false;
                } 
                else if(ev.getStateChange()==ItemEvent.DESELECTED)
                {
                    System.out.println("button is not selected");
                    paused = true;
                    timer.stop();
                }
            }
        });
    }
    
    public void showTileViewButton()
    {
        tileViewButton.setEnabled(true);
        fullViewButton.setEnabled(false);
 
    }
    
    public void showFullViewButton()
    {
        tileViewButton.setEnabled(false);
        fullViewButton.setEnabled(true);
    }
    
    public void setDefaultView(JButton b)
    {
        b.setSelected(true);
        b.doClick();
    }
    
    public void old_initScreenshots_pls_delete_later_ty()
    {
//    public void initScreenshots(int numOfImages, String folder, 
//            String filenameStart, String fileExtension, boolean isTileView) throws URISyntaxException
//    {
//        getScreenshots().clear();
//        
//        for (int i = 1; i <= numOfImages; i++)
//        {
//            BufferedImage img = null;
//            try 
//            {
//                String path = "/" + folder + "/" + filenameStart + i + "." + fileExtension;
//                img = ImageIO.read(new File(getClass().getResource(path).toURI()));
//            } catch (IOException e) 
//            {
//                e.printStackTrace();
//            }
//            
//            JLabel label = new JLabel(); 
//            
//            if(isTileView)
//            {
//                label.setSize(300, 210);
//                label.setText("JRT " + i);
//                
//                Font font = label.getFont();
//                // same font but bold
//                Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
//                label.setFont(boldFont);
//            }
//            else
//            {
//                label.setSize(700, 490);
//            }
//            
//            img = toBufferedImage(img.getScaledInstance(label.getWidth(), 
//                    label.getHeight(), Image.SCALE_SMOOTH));
//            
//            ImageIcon imageIcon = new ImageIcon(img);
//            label.setIcon(imageIcon);
//            
//            label.setHorizontalTextPosition(JLabel.CENTER);
//            label.setVerticalTextPosition(JLabel.BOTTOM);
//            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
//            
//            screenshots.add(label);
//        }
//    }
    }
    
    public void initScreenshots(ArrayList<String> filenames, ArrayList<String> labels, boolean isTileView) throws URISyntaxException {
        getScreenshots().clear();

        for (int i = 0; i < filenames.size(); i++) {
            BufferedImage img = null;
            try {
                String path = "/screenshots/" + filenames.get(i) + ".png";
                System.out.println("path:" + path);
                img = ImageIO.read(new File(getClass().getResource(path).toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            JLabel label = new JLabel();

            if (isTileView) {
                label.setSize(300, 210);
                label.setText(labels.get(i));

                Font font = label.getFont();
                // same font but bold
                Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize());
                label.setFont(boldFont);
            } else {
                label.setSize(700, 490);
            }

            img = toBufferedImage(img.getScaledInstance(label.getWidth(),
                    label.getHeight(), Image.SCALE_SMOOTH));

            ImageIcon imageIcon = new ImageIcon(img);
            label.setIcon(imageIcon);

            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            screenshots.add(label);
        }
    }
    
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    
    public void addScreenshotsTileView(int rows, int columns)
    {
        tileViewPanel.removeAll();
        
        tileViewPanel.setLayout(new java.awt.GridLayout(rows, columns));
        
        for(int i = 0; i < getScreenshots().size(); i++)
        {
            tileViewPanel.add(getScreenshots().get(i));
        }
        
        tileViewPanel.revalidate();
        tileViewPanel.repaint();
    }
    
    public void addScreenshotsFullView(int index)
    {
        tileViewPanel.removeAll();
        
        tileViewPanel.add(getScreenshots().get(index));
        
        tileViewPanel.revalidate();
        tileViewPanel.repaint();
    }
    
    public void updatePlaybackButtons()
    {
        updateSkipToStartButton();
        updatePrevButton();
        updateNextButton();
        updateSkipToEndButton();
        updateStopButton();
    }
    
    public void updateSkipToStartButton()
    {
        if(getCurrentIndex() > 0)
        {
            skipToStartButton.setEnabled(true);
        }
        else
        {
            skipToStartButton.setEnabled(false);
        }
    }
    
    public void updatePrevButton()
    {
        if(getCurrentIndex() > 0)
        {
            prevButton.setEnabled(true);
        }
        else
        {
            prevButton.setEnabled(false);
        }
    }
    
    public void updateNextButton()
    {
        if(getCurrentIndex() < getScreenshots().size() - 1)
        {
            nextButton.setEnabled(true);
        }
        else
        {
            nextButton.setEnabled(false);
        }
    }
    
    public void updateSkipToEndButton()
    {
        System.out.println("i " + getCurrentIndex());
        
        if(getCurrentIndex() < getScreenshots().size() - 1)
        {
            System.out.println("true");
            skipToEndButton.setEnabled(true);
        }
        else
        {
            System.out.println("false");
            skipToEndButton.setEnabled(false);
        }
    }
    
    public void updateStopButton()
    {
        if(playOrPauseButton.isSelected())
        {
            stopButton.setEnabled(true);
        }
        else
        {
            stopButton.setEnabled(false);
        }
    }
    
    public void disableButtonsWhilePlaying()
    {
        skipToEndButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        skipToStartButton.setEnabled(false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        elementTypeButtonGroup = new javax.swing.ButtonGroup();
        jMenuItem1 = new javax.swing.JMenuItem();
        viewPanel = new javax.swing.JPanel();
        tileViewScrollPane = new javax.swing.JScrollPane();
        tileViewPanel = new javax.swing.JPanel();
        playbackPanel = new javax.swing.JPanel();
        skipToStartButton = new javax.swing.JButton();
        skipToEndButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        prevButton = new javax.swing.JButton();
        playOrPauseButton = new javax.swing.JToggleButton();
        algoNameLabel = new javax.swing.JLabel();
        viewTypePanel = new javax.swing.JPanel();
        fullViewButton = new javax.swing.JButton();
        viewTypeButtonDummy = new javax.swing.JButton();
        tileViewButton = new javax.swing.JButton();
        frameNumberPanel = new javax.swing.JPanel();
        frameNumberTextField = new javax.swing.JTextField();
        frameNumberLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Filmification");

        viewPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tileViewPanel.setLayout(new java.awt.GridLayout(1, 0));
        tileViewScrollPane.setViewportView(tileViewPanel);

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tileViewScrollPane)
                .addContainerGap())
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(viewPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tileViewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
                .addContainerGap())
        );

        playbackPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        skipToStartButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/skpToStart.png"))); // NOI18N
        skipToStartButton.setToolTipText("Go to first frame");
        skipToStartButton.setPreferredSize(new java.awt.Dimension(40, 40));
        skipToStartButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipToStartButtonActionPerformed(evt);
            }
        });

        skipToEndButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/skpToEnd.png"))); // NOI18N
        skipToEndButton.setToolTipText("Skip to last frame");
        skipToEndButton.setPreferredSize(new java.awt.Dimension(40, 40));
        skipToEndButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipToEndButtonActionPerformed(evt);
            }
        });

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/next.png"))); // NOI18N
        nextButton.setToolTipText("Next frame");
        nextButton.setPreferredSize(new java.awt.Dimension(40, 40));
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/stop.png"))); // NOI18N
        stopButton.setToolTipText("Stop");
        stopButton.setPreferredSize(new java.awt.Dimension(40, 40));
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/prev.png"))); // NOI18N
        prevButton.setToolTipText("Previous frame");
        prevButton.setPreferredSize(new java.awt.Dimension(40, 40));
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        playOrPauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play.png"))); // NOI18N
        playOrPauseButton.setToolTipText("Play");
        playOrPauseButton.setPreferredSize(new java.awt.Dimension(40, 40));
        playOrPauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playOrPauseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playbackPanelLayout = new javax.swing.GroupLayout(playbackPanel);
        playbackPanel.setLayout(playbackPanelLayout);
        playbackPanelLayout.setHorizontalGroup(
            playbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, playbackPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(skipToStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(playOrPauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(skipToEndButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        playbackPanelLayout.setVerticalGroup(
            playbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, playbackPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(playbackPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(playOrPauseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prevButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skipToEndButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skipToStartButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        algoNameLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        algoNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        algoNameLabel.setText("Jacobi Relaxation Technique");

        viewTypePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        fullViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/fullView.png"))); // NOI18N
        fullViewButton.setToolTipText("Full View");
        fullViewButton.setPreferredSize(new java.awt.Dimension(40, 40));
        fullViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fullViewButtonActionPerformed(evt);
            }
        });

        viewTypeButtonDummy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/eye.png"))); // NOI18N
        viewTypeButtonDummy.setToolTipText("Tile View");
        viewTypeButtonDummy.setFocusable(false);
        viewTypeButtonDummy.setPreferredSize(new java.awt.Dimension(40, 40));
        viewTypeButtonDummy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewTypeButtonDummyActionPerformed(evt);
            }
        });

        tileViewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/tileView.png"))); // NOI18N
        tileViewButton.setToolTipText("Tile View");
        tileViewButton.setPreferredSize(new java.awt.Dimension(40, 40));
        tileViewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tileViewButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout viewTypePanelLayout = new javax.swing.GroupLayout(viewTypePanel);
        viewTypePanel.setLayout(viewTypePanelLayout);
        viewTypePanelLayout.setHorizontalGroup(
            viewTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewTypePanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(tileViewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewTypeButtonDummy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(fullViewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        viewTypePanelLayout.setVerticalGroup(
            viewTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, viewTypePanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(viewTypePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tileViewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewTypeButtonDummy, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullViewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        frameNumberPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        frameNumberTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        frameNumberTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        frameNumberTextField.setFocusable(false);
        frameNumberTextField.setPreferredSize(new java.awt.Dimension(50, 40));

        frameNumberLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        frameNumberLabel.setText("Frame Number:");

        javax.swing.GroupLayout frameNumberPanelLayout = new javax.swing.GroupLayout(frameNumberPanel);
        frameNumberPanel.setLayout(frameNumberPanelLayout);
        frameNumberPanelLayout.setHorizontalGroup(
            frameNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameNumberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(frameNumberLabel)
                .addGap(10, 10, 10)
                .addComponent(frameNumberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        frameNumberPanelLayout.setVerticalGroup(
            frameNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameNumberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameNumberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frameNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frameNumberLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        fileMenu.setText("Film");
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });
        fileMenu.add(jSeparator1);

        jMenuItem8.setText("New Film");
        fileMenu.add(jMenuItem8);
        fileMenu.add(jSeparator2);

        jMenuItem10.setText("Open Film");
        fileMenu.add(jMenuItem10);

        jMenuItem11.setText("Open Recent Film");
        fileMenu.add(jMenuItem11);

        jMenu4.setText("Open Sample Films");

        jMenuItem18.setText("Jacobi Relaxation Technique");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem18);

        jMenuItem19.setLabel("Multistage Network");
        jMenuItem19.setName(""); // NOI18N
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        fileMenu.add(jMenu4);
        fileMenu.add(jSeparator3);

        jMenuItem9.setText("Save");
        fileMenu.add(jMenuItem9);

        jMenuItem12.setText("Save as...");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem12);
        fileMenu.add(jSeparator4);

        jMenu2.setText("Insert Film");

        jMenuItem4.setText("Prepend");
        jMenu2.add(jMenuItem4);

        jMenuItem5.setText("Append");
        jMenu2.add(jMenuItem5);

        fileMenu.add(jMenu2);
        fileMenu.add(jSeparator5);

        jMenuItem15.setText("Exit");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem15);

        menuBar.add(fileMenu);

        jMenu3.setText("Edit");

        jMenuItem6.setText("Undo");
        jMenu3.add(jMenuItem6);

        jMenuItem7.setText("Redo");
        jMenu3.add(jMenuItem7);
        jMenu3.add(jSeparator6);

        jMenuItem13.setText("Cut");
        jMenu3.add(jMenuItem13);

        jMenuItem14.setText("Copy");
        jMenu3.add(jMenuItem14);

        jMenuItem16.setText("Paste");
        jMenu3.add(jMenuItem16);

        jMenuItem17.setText("Select All");
        jMenu3.add(jMenuItem17);

        menuBar.add(jMenu3);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(423, 423, 423)
                .addComponent(algoNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(422, 422, 422))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(viewTypePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(playbackPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(frameNumberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(frameNumberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(algoNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(playbackPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(viewTypePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        setSize(new java.awt.Dimension(1034, 573));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void fullViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fullViewButtonActionPerformed
        updatePlaybackButtons();
        //NOTE
        ArrayList<String> filenames = new ArrayList<String>();
        ArrayList<String> labels = new ArrayList<String>();
        if(filmSelected == "MultistageNetwork"){
            filenames = mn.getScreenshots();
            labels = mn.getLabels();
        } else if (filmSelected == "JacobiRelaxation"){
            //add imoha dri benj
        }
        try 
        {
            initScreenshots(filenames, labels, false);
            
            tileViewPanel.setLayout(new java.awt.GridLayout(1, 1));
        
            showTileViewButton();
            
            playbackPanel.setVisible(true);
            frameNumberPanel.setVisible(true);
            
            addScreenshotsFullView(0);
            
            setCurrentIndex(0);
            frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                    + "/" + getNumOfImages());
        } catch (URISyntaxException ex) 
        {
            Logger.getLogger(FilmificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        updatePlaybackButtons();
    }//GEN-LAST:event_fullViewButtonActionPerformed
    
    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        updatePlaybackButtons();
        
        if((getCurrentIndex() - 1) >= 0)
        {
            addScreenshotsFullView(getCurrentIndex() - 1);
            setCurrentIndex(getCurrentIndex() - 1);
            frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                    + "/" + getNumOfImages());
        }
        
        updatePlaybackButtons();
    }//GEN-LAST:event_prevButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        updatePlaybackButtons();
        
        if((getCurrentIndex() + 1) < getNumOfImages())
        {
            setCurrentIndex(getCurrentIndex() + 1);
            addScreenshotsFullView(getCurrentIndex());
            frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                    + "/" + getNumOfImages());
        }
        
        updatePlaybackButtons();
    }//GEN-LAST:event_nextButtonActionPerformed
    
    private void tileViewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tileViewButtonActionPerformed
        //NOTE
        ArrayList<String> filenames = new ArrayList<String>();
        ArrayList<String> labels = new ArrayList<String>();
        if(filmSelected == "MultistageNetwork"){
            filenames = mn.getScreenshots();
            labels = mn.getLabels();
        } else if (filmSelected == "JacobiRelaxation"){
            //add imoha dri benj
        }
        try
        {
            initScreenshots(filenames, labels, true);
            addScreenshotsTileView(4, 3);
            
            showFullViewButton();
            
            playbackPanel.setVisible(false);
            frameNumberPanel.setVisible(false);
            
        } catch (URISyntaxException ex)
        {
            Logger.getLogger(FilmificationGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tileViewButtonActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fileMenuActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void skipToStartButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipToStartButtonActionPerformed
        updatePlaybackButtons();
        
        setCurrentIndex(0);
        addScreenshotsFullView(getCurrentIndex());
        frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                + "/" + getNumOfImages());
        
        updatePlaybackButtons();
    }//GEN-LAST:event_skipToStartButtonActionPerformed

    private void skipToEndButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipToEndButtonActionPerformed
        updatePlaybackButtons();
        
        setCurrentIndex(getNumOfImages() - 1);
        addScreenshotsFullView(getCurrentIndex());
        frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                + "/" + getNumOfImages());
        
        updatePlaybackButtons();
    }//GEN-LAST:event_skipToEndButtonActionPerformed

    private void viewTypeButtonDummyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewTypeButtonDummyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewTypeButtonDummyActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        updatePlaybackButtons();
        
        timer.stop();
        
        tileViewPanel.removeAll();
        tileViewPanel.add(getScreenshots().get(0));
        frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                            + "/" + getNumOfImages());
        setCurrentIndex(0);
        viewPanel.revalidate();
        viewPanel.repaint();
        
        if(!paused)
        {
            playOrPauseButton.doClick();
            
        }
        
        frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                            + "/" + getNumOfImages());
        
        System.out.println("stop");
        updatePlaybackButtons();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void playOrPauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playOrPauseButtonActionPerformed
        updateStopButton();
        
        timer = new Timer(1000, null);
        
        if(playOrPauseButton.isSelected())
        {
            tileViewPanel.setLayout(new java.awt.GridLayout(1, 1));
            JComponent myComponent = tileViewPanel;
            
            disableButtonsWhilePlaying();
            
            playOrPauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/pause.png"))); // NOI18N
            playOrPauseButton.setToolTipText("Pause");
            
            timer.start();

            timer.addActionListener(new ActionListener() 
            {
                public void actionPerformed(ActionEvent evt) 
                {
                    System.out.println("i " + getCurrentIndex());
                    
                    if(getCurrentIndex() == getScreenshots().size())
                    {
                        timer.stop();
                        
                        setCurrentIndex(0);
                        playOrPauseButton.doClick();
                        updatePlaybackButtons();
                    }

                    tileViewPanel.removeAll();
                    tileViewPanel.add(getScreenshots().get(getCurrentIndex()));
                    frameNumberTextField.setText(String.valueOf(getCurrentIndex() + 1) 
                            + "/" + getNumOfImages());
                    setCurrentIndex(getCurrentIndex() + 1);

                    myComponent.revalidate();
                    myComponent.repaint();
                }
            });
        }
        else
        {
            System.out.println("pause");
            
            playOrPauseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/play.png"))); // NOI18N
            playOrPauseButton.setToolTipText("Play");
            
            updatePlaybackButtons();
        }
    }//GEN-LAST:event_playOrPauseButtonActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        SaveGUI s = new SaveGUI();
        s.setVisible(true);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        filmSelected = "JacobiRelaxation";
        System.out.println("selected film: " + filmSelected);
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        filmSelected = "MultistageNetwork";
        System.out.println("selected film: " + filmSelected);
    }//GEN-LAST:event_jMenuItem19ActionPerformed
    
    // --------------- getters and setters --------------- //

    public ArrayList<JLabel> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(ArrayList<JLabel> screenshots) {
        this.screenshots = screenshots;
    }

    public int getNumOfImages() {
        return numOfImages;
    }

    public void setNumOfImages(int numOfImages) {
        this.numOfImages = numOfImages;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel algoNameLabel;
    private javax.swing.ButtonGroup elementTypeButtonGroup;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JLabel frameNumberLabel;
    private javax.swing.JPanel frameNumberPanel;
    private javax.swing.JTextField frameNumberTextField;
    private javax.swing.JButton fullViewButton;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton nextButton;
    private javax.swing.JToggleButton playOrPauseButton;
    private javax.swing.JPanel playbackPanel;
    private javax.swing.JButton prevButton;
    private javax.swing.JButton skipToEndButton;
    private javax.swing.JButton skipToStartButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton tileViewButton;
    private javax.swing.JPanel tileViewPanel;
    private javax.swing.JScrollPane tileViewScrollPane;
    private javax.swing.JPanel viewPanel;
    private javax.swing.JButton viewTypeButtonDummy;
    private javax.swing.JPanel viewTypePanel;
    // End of variables declaration//GEN-END:variables
}