

#!/bin/bash

#set -e

# Параметры
CONTAINER_ID="${1:-9ea}"

print_info "2. Останавливаем контейнер $CONTAINER_ID..."
    if docker stop "$CONTAINER_ID" &> /dev/null; then
        print_success "Контейнер остановлен"
    else
        print_warning "Контейнер уже остановлен или не найден"
    fi