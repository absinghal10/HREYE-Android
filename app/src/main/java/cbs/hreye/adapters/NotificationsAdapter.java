package cbs.hreye.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cbs.hreye.R;
import cbs.hreye.pojo.NotificationPojo;

/**
 * Created by ANDROID-PC on 13/11/19
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

     Context mContext;
     ArrayList<NotificationPojo> alNotification;
    private int lineCount;
    //private NotificationsAdapterListener listener;
     SparseBooleanArray selectedItems;
     SparseBooleanArray animationItemsIndex;
     boolean reverseAllAnimations = false;
     static int currentSelectedIndex = -1;


    public NotificationsAdapter(Context context) {
        this.mContext = context;
        alNotification = new ArrayList<>();
        //this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.notification_item, parent, false);
        return new MyViewHolder(itemView);
    }

    public void addToArray(NotificationPojo notificationPojo) {
          alNotification.add(notificationPojo);
    }

    @Override
    public int getItemCount() {
        return alNotification.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         final NotificationPojo model = alNotification.get(position);
         if (model.getStatus().equals("0")) {
             holder.tvTitle.setText(model.getTittle());
             holder.tvIconText.setText(model.getTittle().substring(0, 1));
             holder.tvDesc.setText(model.getDescription());
             holder.tvDateTime.setText(model.getDateTime());
             readMoreLess(holder);
             applyProfilePicture(holder, model);
         } else {
             holder.itemView.setVisibility(View.GONE);
             holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
         }

        // setClickListner(holder, model, position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
         TextView tvTitle, tvDesc, tvDateTime, tvReadMore, tvReadLess, tvIconText;
         LinearLayout llMsgContainer;
         ImageView ivLogo;
         RelativeLayout rlIconBack, rlIconFront, rlIconContainer;
        //private CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_tittle);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            tvReadMore = itemView.findViewById(R.id.tv_read_more);
            tvReadLess = itemView.findViewById(R.id.tv_read_less);
            tvIconText = itemView.findViewById(R.id.icon_text);
            ivLogo = itemView.findViewById(R.id.icon_profile);
            //rlIconBack = itemView.findViewById(R.id.icon_back);
            rlIconFront = itemView.findViewById(R.id.icon_front);
            llMsgContainer = itemView.findViewById(R.id.ll_message_container);
            rlIconContainer = itemView.findViewById(R.id.icon_container);
            //itemView.setOnLongClickListener(this);

        }
      /*  @Override
        public boolean onLongClick(View v) {
            listener.onRowLongClicked(getAdapterPosition());
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }*/
    }

    private void applyProfilePicture(MyViewHolder holder, NotificationPojo model) {
        holder.ivLogo.setImageResource(R.drawable.rounded_select);
        if (model.getTittle().startsWith("L")) {
            holder.ivLogo.setColorFilter(ContextCompat.getColor(mContext, R.color.orange), PorterDuff.Mode.MULTIPLY);
        }else {
            holder.ivLogo.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        }
        holder.tvIconText.setVisibility(View.VISIBLE);
    }

    //read more and less function.
    private void readMoreLess(final NotificationsAdapter.MyViewHolder holder) {
        holder.tvDesc.post(new Runnable() {
            @Override
            public void run() {
                lineCount = holder.tvDesc.getLineCount();
                if (lineCount > 3) {
                    holder.tvReadMore.setText(R.string.read_more);
                    holder.tvReadMore.setVisibility(View.VISIBLE);
                } else {
                    holder.tvReadLess.setText(R.string.read_less);
                    holder.tvReadMore.setVisibility(View.GONE);
                }
            }
        });

        holder.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvReadLess.setVisibility(View.VISIBLE);
                holder.tvReadMore.setVisibility(View.GONE);
                holder.tvDesc.setMaxLines(Integer.MAX_VALUE);
            }
        });
        holder.tvReadLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tvReadMore.setVisibility(View.VISIBLE);
                holder.tvReadLess.setVisibility(View.GONE);
                holder.tvDesc.setMaxLines(2);
            }
        });

    }

/*
    private void setClickListner(final MyViewHolder holder, NotificationPojo model, final int position){
        holder.itemView.setActivated(selectedItems.get(position, false));
        applyProfilePicture(holder, model);
        applyIconAnimation(holder, position);
        holder.rlIconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
                holder.tvReadMore.setVisibility(View.VISIBLE);
                holder.tvReadLess.setVisibility(View.GONE);
                holder.tvDesc.setMaxLines(2);
            }
        });

        holder.llMsgContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
                setMoreLessDesc(holder);
            }
        });

        holder.llMsgContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                setMoreLessDesc(holder);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    private void applyProfilePicture(MyViewHolder holder, NotificationPojo model) {
        holder.ivLogo.setImageResource(R.drawable.rounded_select);
        if (model.getDescription().substring(0, 1).equals("A")) {
            holder.ivLogo.setColorFilter(ContextCompat.getColor(mContext, R.color.orange), PorterDuff.Mode.MULTIPLY);
        }else {
            holder.ivLogo.setColorFilter(ContextCompat.getColor(mContext, R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
        }
        holder.tvIconText.setVisibility(View.VISIBLE);
    }

    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.rlIconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.rlIconBack);
            holder.rlIconBack.setVisibility(View.VISIBLE);
            holder.rlIconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.rlIconBack, holder.rlIconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.rlIconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.rlIconFront);
            holder.rlIconFront.setVisibility(View.VISIBLE);
            holder.rlIconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.rlIconBack, holder.rlIconFront, false);
                resetCurrentIndex();
            }
        }
    }

    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeData(int position) {
        alNotification.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }



    private void setMoreLessDesc(MyViewHolder holder) {
        holder.tvReadMore.setVisibility(View.VISIBLE);
        holder.tvReadLess.setVisibility(View.GONE);
        holder.tvDesc.setMaxLines(2);
    }

    public interface NotificationsAdapterListener {
        void onIconClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }*/
}