#!/bin/bash
set -e
#cd user-project
#git pull

sleep .5

#Collect history branches names starting from
historyBranches=$(git branch -r | grep 'origin/' | sed 's/origin\///')
echo "History branches: $historyBranches"

if [ -z "$historyBranches" ]; then
 echo "No branches with prefix '$historyBranchNamePrefix' found"
 exit 0
fi

exit 0

# Выводим список веток
echo "Found branches:"
echo "$historyBranches"

#Find latest date branch
latest_branch=$(echo "$historyBranches" | sort -r | head -n 1)
echo "Latest branch to keep:  $latest_branch"

for branch in $historyBranches; do
  if [[ "$branch" != "$latestBranch" ]]; then
    echo "Deleting branch: $branch"
    #git push origin --delete "$branch" || echo "Failed to delete branch: $branch"
  else
    echo "Skipping branch: $branch (latest)"
  fi
done

echo "All  branches have been deleted, except the latest one."