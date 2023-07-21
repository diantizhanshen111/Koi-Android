// Generated by view binder compiler. Do not edit!
package com.chat.uikit.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.chat.base.ui.components.AvatarView;
import com.chat.uikit.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemSearchChannelLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AvatarView avatarView;

  @NonNull
  public final TextView contentTv;

  @NonNull
  public final TextView nameTv;

  private ItemSearchChannelLayoutBinding(@NonNull LinearLayout rootView,
      @NonNull AvatarView avatarView, @NonNull TextView contentTv, @NonNull TextView nameTv) {
    this.rootView = rootView;
    this.avatarView = avatarView;
    this.contentTv = contentTv;
    this.nameTv = nameTv;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemSearchChannelLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemSearchChannelLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_search_channel_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemSearchChannelLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.avatarView;
      AvatarView avatarView = ViewBindings.findChildViewById(rootView, id);
      if (avatarView == null) {
        break missingId;
      }

      id = R.id.contentTv;
      TextView contentTv = ViewBindings.findChildViewById(rootView, id);
      if (contentTv == null) {
        break missingId;
      }

      id = R.id.nameTv;
      TextView nameTv = ViewBindings.findChildViewById(rootView, id);
      if (nameTv == null) {
        break missingId;
      }

      return new ItemSearchChannelLayoutBinding((LinearLayout) rootView, avatarView, contentTv,
          nameTv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}