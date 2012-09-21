#!/bin/bash

cd $(dirname "$0");

IGNORE_COMMITS_FILE="../.ignored_commits_from_kilkari_to_master";
SINCE_BRANCH="origin/master";
UNTIL_BRANCH="origin/ananya-kilkari";

UNMERGED_COMMITS=$(git log --oneline $SINCE_BRANCH..$UNTIL_BRANCH);

if [[ -e "$IGNORE_COMMITS_FILE" ]]; then
  IGNORED_COMMITS=$(cat $IGNORE_COMMITS_FILE | grep [a-zA-Z0-9]); 
  if [[ $IGNORED_COMMITS != "" ]]; then
    UNMERGED_COMMITS=$(echo "$UNMERGED_COMMITS" | grep -v "$IGNORED_COMMITS");
  fi
fi

UNMERGED_COMMITS=$(echo "$UNMERGED_COMMITS" | grep [a-zA-Z0-9]);

if [[ "$UNMERGED_COMMITS" =  "" ]]; then
  echo "#### NO UNMERGED COMMITS FOUND";
  exit 0;
fi

NUM_NOT_MERGED=$(echo "$UNMERGED_COMMITS" | wc -l);

echo "#### NUMBER OF UNMERGED COMMITS: $NUM_NOT_MERGED";
echo "######################## UNMERGED COMMITS FROM MASTER - START ###########################";
echo "$UNMERGED_COMMITS";
echo "######################## UNMERGED COMMITS FROM MASTER - END #############################";
exit 1;
