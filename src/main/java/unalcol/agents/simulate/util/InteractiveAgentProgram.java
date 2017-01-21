package unalcol.agents.simulate.util;


import unalcol.agents.Action;
import unalcol.agents.AgentProgram;
import unalcol.agents.Percept;
import unalcol.types.collection.vector.Vector;


public class InteractiveAgentProgram implements AgentProgram {
    public static InteractiveAgentFrame frame = null;
    protected Vector<String> cmds = new Vector<String>();
    protected Language language;

    public InteractiveAgentProgram(Language _language) {
        language = _language;
        if (frame == null) {
            frame = new InteractiveAgentFrame(this);
        }
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new InteractiveAgentProgram(null);
    }

    public Action compute(Percept p) {
        Action x = null;
        if (cmds.size() > 0) {
            x = new Action(cmds.get(0));
            cmds.remove(0);
        }
        return x;
    }

    public void setCommands(String _cmds) {
        String[] actions = _cmds.split(",");
        for (int i = 0; i < actions.length; i++) {
            cmds.add(actions[i]);
        }
    }

    public void init() {
        cmds.clear();
    }

    public boolean goalAchieved(Percept p) {
        return (((Boolean) p.getAttribute(language.getPercept(4))).booleanValue());
    }

}
