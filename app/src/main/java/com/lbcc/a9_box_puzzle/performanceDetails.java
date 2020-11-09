package com.lbcc.a9_box_puzzle;

public class performanceDetails {
    String mPlayerName;
    private int mEasyWins=0;
    private int mEasyMoves=0;
    private int mEasyScore=0;
    private int mEasyGamesPlayed=0;
    private int mMediumWins=0;
    private int mMediumMoves=0;
    private int mMediumScore=0;
    private int mMediumGamesPlayed=0;
    private int mHardWins=0;
    private int mHardMoves=0;
    private int mHardScore=0;
    private int mHardGamesPlayed=0;
    private int mTotalScore;
    private float mAvgScore;
    private int mTotalWins;
    private int mTotalGamesPlayed=0;

    //int mEasyWins,int mEasyMoves,int mEasyScore,int mEasyGamesPlayed,int mMediumWins,int mMediumMoves,int mMediumScore,int mMediumGamesPlayed,int mHardWins,int mHardMoves,int mHardScore, int mHardGamesPlayed
    public performanceDetails(String name,int EasyWins,int EasyMoves,int EasyScore,int EasyGamesPlayed,int MediumWins,int MediumMoves,int MediumScore,int MediumGamesPlayed,int HardWins,int HardMoves,int HardScore, int HardGamesPlayed)
    {
         mPlayerName=name;
         mEasyWins=EasyWins;
         mEasyMoves=EasyMoves;
         mEasyScore = EasyScore;
         mEasyGamesPlayed = EasyGamesPlayed;
         mMediumWins= MediumWins;
         mMediumMoves = MediumMoves;
         mMediumScore=MediumScore;
         mMediumGamesPlayed = MediumGamesPlayed;
         mHardWins = HardWins;
         mHardMoves = HardMoves;
         mHardScore = HardScore;
         mHardGamesPlayed = HardGamesPlayed;

        mTotalScore = mEasyScore+mMediumScore+mHardScore;
        mTotalGamesPlayed = mEasyGamesPlayed+mHardGamesPlayed+mMediumGamesPlayed;
        mAvgScore = mTotalScore/mTotalGamesPlayed;
        mTotalWins = mEasyWins+mMediumWins+mHardWins;
    }

    public int getmTotalGamesPlayed() {
        return mTotalGamesPlayed;
    }

    public float getmAvgScore() {
        return mAvgScore;
    }

    public int getmTotalScore() {
        return mTotalScore;
    }

    public int getmTotalWins() {
        return mTotalWins;
    }
}
