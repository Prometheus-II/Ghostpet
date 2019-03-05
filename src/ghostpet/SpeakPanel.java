package ghostpet;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import ghost_behaviors.Behavior;
import ghost_behaviors.Behavior_Ask;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.regex.Pattern;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SwingConstants;

public class SpeakPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7710468722110870181L;

	/**
	 * Create the panel.
	 */
	JLabel SpeakLabel;
	JPanel AskPanel;
	GhostBase parent;
	int EstTextWidth;	
	
	public SpeakPanel(GhostBase p) {
		parent = p;
		EstTextWidth = parent.data.TextBox.getIconWidth() - 70;
		
		setFont(parent.data.font);
		
		setBackground(new Color(0,0,0,0));
		
		setSize(parent.data.TextBox.getIconWidth(), parent.data.TextBox.getIconHeight());
		setMaximumSize(getSize());
		setMinimumSize(getSize());
		setPreferredSize(getSize());
		if(getFont() != null)
			SpeakLabel.setFont(getFont());
		
		GridBagLayout gbl_layeredPane = new GridBagLayout();
		gbl_layeredPane.columnWidths = new int[]{0};
		gbl_layeredPane.rowHeights = new int[]{0};
		gbl_layeredPane.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_layeredPane.rowWeights = new double[]{Double.MIN_VALUE};
		setLayout(gbl_layeredPane);
		
		
		SpeakLabel = new JLabel("SpeakLabel");
		SpeakLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_SpeakLabel = new GridBagConstraints();
		gbc_SpeakLabel.insets = new Insets(15, 15, 5, 0);
		gbc_SpeakLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_SpeakLabel.gridx = 0;
		gbc_SpeakLabel.gridy = 0;
		add(SpeakLabel, gbc_SpeakLabel);
		
		AskPanel = new JPanel();
		AskPanel.setBackground(new Color(0,0,0,0));
		GridBagConstraints gbc_AskPanel = new GridBagConstraints();
		gbc_AskPanel.weighty = 1.0;
		gbc_AskPanel.anchor = GridBagConstraints.NORTH;
		gbc_AskPanel.fill = GridBagConstraints.BOTH;
		gbc_AskPanel.gridx = 0;
		gbc_AskPanel.gridy = 1;
		gbc_AskPanel.insets = new Insets(0,15,0,0);
		add(AskPanel, gbc_AskPanel);
		
		GridBagLayout gbl_AskPanel = new GridBagLayout();
		gbl_AskPanel.columnWidths = new int[]{0, 0};
		gbl_AskPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_AskPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_AskPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		AskPanel.setLayout(gbl_AskPanel);
		
		AskPanel.setVisible(false);
		setVisible(false);
	}
	public void Say(String state, Behavior_Ask.Option[] opts)
	{
		String out = state;
		out = out.replaceAll(Pattern.quote("$NAME$"), parent.data.UserName);
		out = out.replaceAll(Pattern.quote("$GNAME$"), parent.data.ghostName);
		out = "<html><body style='width: "+EstTextWidth+"px'>" + out+"</html>";
		SpeakLabel.setText(out);
		JLabel optLabel;
		GridBagConstraints gbc_optLabel;
		int y = 0;
		for(int i = 0; i < opts.length; i++)
		{
			gbc_optLabel = new GridBagConstraints();
			gbc_optLabel.gridx = 0;
			gbc_optLabel.gridy = y;
			gbc_optLabel.weighty = 0;
			gbc_optLabel.anchor = GridBagConstraints.NORTHWEST;
			gbc_optLabel.insets = new Insets(5, 0, 5, 0);
			out = opts[i].line;
			out = out.replaceAll(Pattern.quote("$NAME$"), parent.data.UserName);
			out = out.replaceAll(Pattern.quote("$GNAME$"), parent.data.ghostName);
			out = "<html><body style='width: "+EstTextWidth+"px'>* "+ out+"</html>";
			optLabel = new AskChoiceLabel<Behavior>(out, opts[i].bhvr);
			optLabel.addMouseListener(new MouseUser(parent));
			if(getFont() != null)
				optLabel.setFont(getFont());
			AskPanel.add(optLabel, gbc_optLabel);
			
			y++;
		}
		Component verticalGlue = Box.createVerticalGlue();
		GridBagConstraints gbc_verticalGlue = new GridBagConstraints();
		gbc_verticalGlue.gridx = 0;
		gbc_verticalGlue.gridy = y;
		AskPanel.add(verticalGlue, gbc_verticalGlue);
				
		AskPanel.setVisible(true);
		setVisible(true);
		updateUI();
	}
	public void Say(String state)
	{
		AskPanel.setVisible(false);
		String out = state;
		out = out.replaceAll(Pattern.quote("$NAME$"), parent.data.UserName);
		out = out.replaceAll(Pattern.quote("$GNAME$"), parent.data.ghostName);
		out = "<html><body style='width: "+EstTextWidth+"px'>" + out+"</html>";
		SpeakLabel.setText(out);
		setVisible(true);
		updateUI();
	}
	
	public void Reset()
	{
		AskPanel.setVisible(false);
		setVisible(false);
		updateUI();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(parent.data.TextBox.getImage(), 0, 0, null);
	}
}
