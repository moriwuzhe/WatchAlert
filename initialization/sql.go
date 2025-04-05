package initialization

import (
	"crypto/md5"
	"encoding/hex"
	"time"
	"watchAlert/internal/models"
	"watchAlert/pkg/ctx"

	"github.com/zeromicro/go-zero/core/logc"
	"gorm.io/gorm"
)

var perms []models.UserPermissions

func InitPermissionsSQL(ctx *ctx.Context) {
	var psData []models.UserPermissions

	for _, v := range models.PermissionsInfo() {
		psData = append(psData, v)
	}
	perms = psData

	ctx.DB.DB().Session(&gorm.Session{AllowGlobalUpdate: true}).Delete(&models.UserPermissions{})
	ctx.DB.DB().Model(&models.UserPermissions{}).Create(&psData)
}

func InitRoleSQL(ctx *ctx.Context) {
	var adminRole models.Role
	var db = ctx.DB.DB().Model(&models.Role{})

	role := models.Role{
		ID:          "admin",
		Name:        "admin",
		Description: "system administrator",
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	err := db.Where("name = ?", "admin").First(&adminRole).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			err = ctx.DB.DB().Create(&role).Error
		}
	}

	if err != nil {
		logc.Errorf(ctx.Ctx, err.Error())
		panic(err)
	}
}

func InitUserRolesSQL(ctx *ctx.Context) {
	var adminRole models.UserRole
	var db = ctx.DB.DB().Model(&models.UserRole{})

	roles := models.UserRole{
		ID:          "admin",
		RoleID:      "admin",
		UserID:      "admin",
		Name:        "admin",
		Description: "system",
		Permissions: perms,
		CreateAt:    time.Now().Unix(),
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	err := db.Where("name = ?", "admin").First(&adminRole).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			err = ctx.DB.DB().Create(&roles).Error
		}
	} else {
		err = db.Where("name = ?", "admin").Updates(models.UserRole{Permissions: perms}).Error
	}

	if err != nil {
		logc.Errorf(ctx.Ctx, err.Error())
		panic(err)
	}
}

func InitUserSQL(ctx *ctx.Context) {
	var adminUser models.Member
	var db = ctx.DB.DB().Model(&models.Member{})

	// 创建MD5密码
	arr := md5.Sum([]byte("admin"))
	hashPassword := hex.EncodeToString(arr[:])

	user := models.Member{
		UserId:   "admin",
		UserName: "admin",
		Password: hashPassword,
		Email:    "admin@example.com",
		Phone:    "",
		CreateBy: "system",
		CreateAt: time.Now().Unix(),
		Tenants:  []string{"default"},
	}

	err := db.Where("user_id = ?", "admin").First(&adminUser).Error
	if err != nil {
		if err == gorm.ErrRecordNotFound {
			err = ctx.DB.DB().Create(&user).Error
		}
	}

	if err != nil {
		logc.Errorf(ctx.Ctx, err.Error())
		panic(err)
	}
}
