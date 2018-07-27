// Generated code from Butter Knife. Do not modify!
package com.chicsol.marrymax.adapters;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.chicsol.marrymax.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChildAdapter$ViewHolder_ViewBinding implements Unbinder {
  private ChildAdapter.ViewHolder target;

  @UiThread
  public ChildAdapter$ViewHolder_ViewBinding(ChildAdapter.ViewHolder target, View source) {
    this.target = target;

    target.tvChild = Utils.findRequiredViewAsType(source, R.id.tv_child, "field 'tvChild'", TextView.class);
    target.rlHead = Utils.findRequiredViewAsType(source, R.id.RelativeLayoutHeadSubItem, "field 'rlHead'", LinearLayout.class);
    target.llContent = Utils.findRequiredViewAsType(source, R.id.LinearLayoutContent, "field 'llContent'", LinearLayout.class);
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.TextViewQuestionTitle, "field 'tvTitle'", TextView.class);
    target.cb = Utils.findRequiredViewAsType(source, R.id.CheckboxQuestionTitle, "field 'cb'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChildAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvChild = null;
    target.rlHead = null;
    target.llContent = null;
    target.tvTitle = null;
    target.cb = null;
  }
}
