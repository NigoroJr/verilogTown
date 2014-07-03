import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;

//extend undomanager to handle the undo and redo
public class MyUndo1 extends UndoManager
{
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e)
	{
		//only if the change is not belongs to style change, it remembered by the stuck
		if(!e.getEdit().getPresentationName().equals("style change"))
		{
			this.addEdit(e.getEdit());
		}
	}
	@Override
	public void trimEdits(int from, int to)
	{
		super.trimEdits(from,to);
	}
}