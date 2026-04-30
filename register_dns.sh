#!/bin/bash
set -e

API_URL="https://webapps.cs.moravian.edu/awsdns/setip"

# -----------------------------
# Fetch secrets from AWS
# -----------------------------
SECRET_JSON=$(aws secretsmanager get-secret-value \
  --secret-id 220_DNS_Token \
  --query SecretString \
  --output text)

if [ -z "$SECRET_JSON" ]; then
  echo "ERROR: Failed to retrieve secrets"
  exit 1
fi

USERNAME=$(echo "$SECRET_JSON" | jq -r '.USERNAME')
LABEL=$(echo "$SECRET_JSON" | jq -r '.LABEL')
TOKEN=$(echo "$SECRET_JSON" | jq -r '.TOKEN')

if [ -z "$USERNAME" ] || [ -z "$LABEL" ] || [ -z "$TOKEN" ]; then
  echo "ERROR: Missing required secret fields"
  exit 1
fi

# -----------------------------
# Get EC2 public IP
# -----------------------------
PUBLIC_IP=$(curl -s --max-time 5 http://169.254.169.254/latest/meta-data/public-ipv4)

if [ -z "$PUBLIC_IP" ]; then
  echo "ERROR: Could not retrieve public IP"
  exit 1
fi

# -----------------------------
# Register with DNS system
# -----------------------------
RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$API_URL" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"$USERNAME\",
    \"label\": \"$LABEL\",
    \"ipAddress\": \"$PUBLIC_IP\"
  }")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" -eq 200 ]; then
    exit 0
else
    echo "ERROR: DNS registration failed ($HTTP_CODE)"
    echo "$BODY"
    exit 1
fi
